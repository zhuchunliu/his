package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.ZhangYaoConstant;
import com.acmed.his.dao.ZhangYaoMapper;
import com.acmed.his.dao.ZyAddressMapper;
import com.acmed.his.dao.ZyOrderItemMapper;
import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.ZyAddress;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.model.ZyOrderItem;
import com.acmed.his.model.dto.ZyOrderItemDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.zy.*;
import com.acmed.his.service.CommonManager;
import com.acmed.his.service.ZhangYaoManager;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.UUIDUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-05-22
 **/
@Service
public class ZhangYaoOrderManager  implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(ZhangYaoManager.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ZhangYaoMapper zhangYaoMapper;

    @Autowired
    private ZyOrderMapper zyOrderMapper;

    @Autowired
    private ZyOrderItemMapper zyOrderItemMapper;

    @Autowired
    private ZyAddressMapper zyAddressMapper;

    @Autowired
    private CommonManager commonManager;

    private static String ZHANGYAO_URL = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.ZHANGYAO_URL = environment.getProperty("zhangyao.url");
    }

    /**
     * 待提交订单
     * @param info
     */
    @Transactional
    public List<ZyOrderItemDto> getUnSubmitOrder(UserInfo info,String name) {

        List<PrescriptionItem> itemList = zhangYaoMapper.getUnDismantleList(info.getOrgCode());
        List<String> itemIdList = Lists.newArrayList();
        if(null != itemList && 0 != itemList.size()){

            synchronized ((Object)info.getOrgCode().intValue()){

                // step1:根据药店分组
                List<String> storeIdList = Lists.newArrayList();
                Map<String,List<PrescriptionItem>> map = Maps.newHashMap();
                itemList.forEach(item->{
                    if(!map.containsKey(item.getZyStoreId())){
                        map.put(item.getZyStoreId(),Lists.newArrayList());
                    }
                    map.get(item.getZyStoreId()).add(item);
                    storeIdList.add(item.getZyStoreId());
                    itemIdList.add(item.getId());
                });


                //step2:根据分组找到未付款订单， 进行合并
                List<ZyOrder> unpaidOrderList = zhangYaoMapper.getUnSubmitOrder(info.getOrgCode(),storeIdList);
                for(ZyOrder order : unpaidOrderList){
                    if(!map.containsKey(order.getZyStoreId())) continue;
                    for(PrescriptionItem item : map.get(order.getZyStoreId())){
                        ZyOrderItem orderItem = this.getZyOrderItem(order,item);
                        zyOrderItemMapper.insert(orderItem);
                        order.setDrugFee(order.getDrugFee()+orderItem.getFee());
                    }
                    order.setFee(order.getDrugFee()+order.getExpressFee());
                    zyOrderMapper.updateByPrimaryKey(order);
                    map.remove(order.getZyStoreId());
                }

                //step3 建立新的订单
                Iterator<String> iterator = map.keySet().iterator();
                while (iterator.hasNext()){
                    ZyOrder order = new ZyOrder();
                    order.setId(UUIDUtil.generate());
                    order.setZyStoreId(iterator.next());
                    order.setOrgCode(info.getOrgCode());
                    order.setOrderNo(LocalDate.now().toString().replaceAll("-","")+
                            commonManager.getFormatVal(info.getOrgCode() + LocalDate.now().toString(), "00000"));
                    order.setPayStatus(0);
                    order.setIsRecepit(0);
                    order.setExpressFee(0d);
                    order.setRemoved("0");
                    order.setCreateAt(LocalDate.now().toString());
                    order.setCreateBy(info.getId().toString());

                    for(PrescriptionItem item : map.get(order.getZyStoreId())){
                        ZyOrderItem orderItem = this.getZyOrderItem(order,item);
                        zyOrderItemMapper.insert(orderItem);
                        order.setZyStoreName(item.getZyStoreName());
                        order.setDrugFee(Optional.ofNullable(order.getDrugFee()).orElse(0d)+orderItem.getFee());
                    }
                    order.setFee(order.getDrugFee()+order.getExpressFee());
                    zyOrderMapper.insert(order);
                }

                //step4: 更新订单状态为已拆单状态
                zhangYaoMapper.updateItemDismantleStatus(itemIdList);

            }
        }
        return zhangYaoMapper.getUnSubmitOrderItem(info.getOrgCode(),name);

    }

    /**
     * 删除待提交订单
     * @param info
     * @param orderId
     * @param itemId
     */
    @Transactional
    public void delUnSubmitOrder(UserInfo info, String orderId, String itemId) {
        synchronized ((Object)info.getOrgCode().intValue()) {
            if (StringUtils.isNotEmpty(orderId)) {
                ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(orderId);
                if(0 != zyOrder.getPayStatus()){
                    throw new BaseException(StatusCode.FAIL,"订单已经被提交,禁止删除");
                }
                zyOrder.setRemoved("1");
                zyOrder.setModifyAt(LocalDateTime.now().toString());
                zyOrder.setModifyBy(info.getId().toString());
                zyOrderMapper.updateByPrimaryKey(zyOrder);

                zyOrderItemMapper.deleteByOrderId(orderId);
            }

            if (StringUtils.isNotEmpty(itemId)) {
                ZyOrderItem item = zyOrderItemMapper.selectByPrimaryKey(itemId);
                ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(item.getOrderId());
                if(0 != zyOrder.getPayStatus()){
                    throw new BaseException(StatusCode.FAIL,"订单已经被提交,禁止删除");
                }
                zyOrderItemMapper.deleteById(itemId);
            }
        }

    }





    private ZyOrderItem getZyOrderItem(ZyOrder order ,PrescriptionItem item){
        ZyOrderItem orderItem = new ZyOrderItem();
        orderItem.setId(UUIDUtil.generate());
        orderItem.setOrderId(order.getId());
        orderItem.setApplyId(item.getApplyId());
        orderItem.setPreItemId(item.getId());
        orderItem.setDrugId(item.getZyDrugId());
        orderItem.setDrugName(item.getDrugName());
        orderItem.setNum(item.getNum());
        orderItem.setRetailPrice(item.getRetailPrice());
        orderItem.setFee(item.getFee());
        orderItem.setManufacturerName(item.getZyManufacturerName());
        orderItem.setRemoved("0");
        return orderItem;
    }


    /**
     * 下单
     * @param mo
     * @param info
     */
    @Transactional
    public Integer submit(ZYOrderSubmitPayMo mo, UserInfo info) {

        ZyAddress zyAddress = zyAddressMapper.selectByPrimaryKey(mo.getAddressId());
        StringBuilder builder = new StringBuilder();
        synchronized ((Object)info.getOrgCode().intValue()) {

            //step1: 校验是否被提交
            Map<String,ZyOrder> orderMap = Maps.newHashMap();
            for(ZYOrderSubmitPayMo.ZYOrderSubmitPayDetailMo detailMo : mo.getDetailMoList()){
                ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(detailMo.getOrderId());
                orderMap.put(zyOrder.getId(),zyOrder);

                if(0 != zyOrder.getPayStatus()){//订单已经被人提交
                    builder.append(","+zyOrder.getOrderNo());
                }
            }
            if(StringUtils.isNotEmpty(builder.toString())){
                throw new BaseException(StatusCode.FAIL,builder.toString().substring(1)+"订单已被提交,请勿重复提交订单");
            }

            //step2:校验数据是否正确
            Map<String,List<ZyOrderItem>> orderItemMap = Maps.newHashMap();
            boolean flag = true;
            for(ZYOrderSubmitPayMo.ZYOrderSubmitPayDetailMo detailMo : mo.getDetailMoList()){
                List<ZyOrderItem> itemList = zyOrderItemMapper.getItemByOrderIdExclueRemove(detailMo.getOrderId());
                orderItemMap.put(detailMo.getOrderId(),itemList);

                List<String> itemIdList = Lists.newArrayList();
                itemList.forEach(obj->itemIdList.add(obj.getId()));

                ZyOrder zyOrder = orderMap.get(detailMo.getOrderId());

                zyOrder.setAddressId(zyAddress.getId());
                zyOrder.setProvinceId(zyAddress.getProvinceId());
                zyOrder.setProvinceName(zyAddress.getProvinceName());
                zyOrder.setCityId(zyAddress.getCityId());
                zyOrder.setCityName(zyAddress.getCityName());
                zyOrder.setCountyId(zyAddress.getCountyId());
                zyOrder.setCountyName(zyAddress.getCountyName());
                zyOrder.setAddress(zyAddress.getAddress());
                zyOrder.setRecipient(zyAddress.getRecipient());
                zyOrder.setZipCode(zyAddress.getZipCode());
                zyOrder.setPhone(zyAddress.getPhone());

                zyOrder.setExpressId(detailMo.getExpressId());
                zyOrder.setExpressFee(detailMo.getExpressFee());
                zyOrder.setFee(zyOrder.getDrugFee()+zyOrder.getExpressFee());
                zyOrder.setPayStatus(1);
                zyOrder.setModifyBy(info.getId().toString());
                zyOrder.setModifyAt(LocalDateTime.now().toString());
                zyOrderMapper.updateByPrimaryKey(zyOrder);

                if(!detailMo.getItemIdList().containsAll(itemIdList) || !itemIdList.containsAll(detailMo.getItemIdList())){
                    flag = false;
                    zyOrderItemMapper.updateNormalStatus(zyOrder.getId(),detailMo.getItemIdList());//过滤掉removed=1,设置成正常状态
                    zyOrderItemMapper.updateAddStatus(zyOrder.getId(),detailMo.getItemIdList());//过滤掉当前的详情,设置成新增状态
                    zyOrderItemMapper.updateRemoveStatus(zyOrder.getId(),detailMo.getItemIdList());//过滤removed=1,设置成删除状态
                }else{
                    zyOrderItemMapper.updateNormalStatus(zyOrder.getId(),detailMo.getItemIdList());
                }
            }
            if(!flag){
//                throw new BaseException(StatusCode.FAIL,"订单提交失败，数据已被刷新，请到待支付重新提交");
                return -1;
            }

            //setp3:给掌药提交数据,并验证


        }
        return null;
    }




    /**
     * 获取下单列表
     * @param pageBase
     * @param info
     * @return
     */
    public PageResult getOrderList(PageBase<ZyOrderQueryMo> pageBase, UserInfo info) {
        Page page= PageHelper.startPage(pageBase.getPageNum(),pageBase.getPageSize());
        List<ZyOrder> source = zhangYaoMapper.getOrderList(Optional.ofNullable(pageBase.getParam()).orElse(new ZyOrderQueryMo()),info.getOrgCode());
        List<ZyOrderVo> list = Lists.newArrayList();
        source.forEach(obj->{
            ZyOrderVo vo = new ZyOrderVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        PageResult pageResult = new PageResult();
        pageResult.setData(list);
        pageResult.setTotal(page.getTotal());
        return pageResult;

    }

    //    @Transactional
//    public void pay(List<ZYOrderSubmitPayMo> moList, UserInfo user) {
//        for(ZYOrderSubmitPayMo mo : moList){
//            ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(mo.getOrderId());
//            if(!zyOrder.getDrugFee().equals(mo.getDrugFee())){
//                throw new BaseException(StatusCode.FAIL,"订单已被更新，请重新刷新之后付款");
//            }
//            if(!zyOrder.getExpressId().equals(mo.getExpressId())){
//
//            }
//        }
//    }


    /**
     * 获取掌药订单详情
     * @param orderId
     */
    public ZYOrderDetailVo getOrderDetail(String orderId) {
        ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(orderId);
        if (null != zyOrder && StringUtils.isNotEmpty(zyOrder.getZyOrderSn())) {
            StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL);
            builder.append(ZhangYaoConstant.buildOrderQueryUrl(zyOrder.getZyOrderSn()));
            RestTemplate restTemplate = new RestTemplate();
            JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
            if (json.get("code").equals("1")) {
                ZYOrderDetailObj obj = new ZYOrderDetailObj();
                JSONObject data = json.getJSONObject("data");
                obj.setOrderId(data.getString("orderId"));
                obj.setOrderSn(data.getString("orderSn"));
                obj.setPayAmount(data.getString("payAmount"));
                obj.setPayTime(data.getString("payTime"));
                obj.setOrderState(data.getString("orderState"));
                obj.setPhone(data.getString("phone"));

                JSONArray jsonArray = data.getJSONArray("goodsList");
                List<ZYOrderDetailObj.ZYOrderDetailItem> list = Lists.newArrayList();

                List<ZYOrderDetailVo.ZYOrderDetailItemVo> voList = Lists.newArrayList();

                for(int i = 0;i<jsonArray.size();i++) {
                    ZYOrderDetailObj.ZYOrderDetailItem item = JSONObject.parseObject(jsonArray.getString(i), ZYOrderDetailObj.ZYOrderDetailItem.class);
                    list.add(item);
                    ZYOrderDetailVo.ZYOrderDetailItemVo itemVo = new ZYOrderDetailVo.ZYOrderDetailItemVo();
                    BeanUtils.copyProperties(item,itemVo);
                    voList.add(itemVo);
                }
                obj.setGoodsList(list);

                ZYOrderDetailVo vo = new ZYOrderDetailVo();
                BeanUtils.copyProperties(obj, vo,"goodsList");
                vo.setGoodsList(voList);
                return vo;
            }else {
                logger.error("get zhangyao drug fail,msg: " + json.get("message") + "  ;the url is :" + builder.toString());
                throw new BaseException(StatusCode.FAIL, "获取掌药药品信息失败");
            }
        }
        return null;
    }


    /**
     * 确认发药
     * @param id
     * @param user
     */
    public void recepit(String id, UserInfo user) {
        ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(id);
        zyOrder.setIsRecepit(1);
        zyOrder.setModifyAt(LocalDateTime.now().toString());
        zyOrder.setModifyBy(user.getId().toString());
        zyOrderMapper.updateByPrimaryKey(zyOrder);
    }



}
