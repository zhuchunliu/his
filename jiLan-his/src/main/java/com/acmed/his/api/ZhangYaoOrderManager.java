package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.ZhangYaoConstant;
import com.acmed.his.dao.ZhangYaoMapper;
import com.acmed.his.dao.ZyOrderItemMapper;
import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.PrescriptionItem;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

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
public class ZhangYaoOrderManager {

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
    private CommonManager commonManager;

    private static String ZHANGYAO_URL = "zhangyao.url";

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
                    order.setOrderNo(LocalDate.now().toString()+
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



    @Transactional
    public void pay(List<ZYOrderPayMo> moList, UserInfo user) {
        for(ZYOrderPayMo mo : moList){
            ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(mo.getOrderId());
            if(!zyOrder.getDrugFee().equals(mo.getDrugFee())){
                throw new BaseException(StatusCode.FAIL,"订单已被更新，请重新刷新之后付款");
            }
            if(!zyOrder.getExpressId().equals(mo.getExpressId())){

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
     * @param ids
     */
    @Transactional
    public void dismantle(String ids, UserInfo info) {

//        Map<String,List<PrescriptionItem>> map = Maps.newHashMap();
//        itemList.forEach(obj->{
//            if(map.containsKey(obj.getZyStoreId())){
//                map.get(obj.getZyStoreId()).add(obj);
//            }else{
//                map.put(obj.getZyStoreId(), Lists.newArrayList(obj));
//            }
//        });
//
//        Iterator iterator = map.keySet().iterator();
//        while (iterator.hasNext()){
//            List<PrescriptionItem> childList = map.get(iterator.next());
//            ZyOrder zyOrder = new ZyOrder();
//            zyOrder.setId(UUIDUtil.generate());
//            zyOrder.setOrgCode(info.getOrgCode());
//            zyOrder.setZyStoreId(childList.get(0).getZyStoreId());
//            zyOrder.setZyStoreName(childList.get(0).getZyStoreName());
//            zyOrder.setPayStatus(0);
//            zyOrder.setIsRecepit(0);
//            zyOrder.setCreateAt(LocalDateTime.now().toString());
//            zyOrder.setCreateBy(info.getId().toString());
//
//            zyOrderMapper.insert(zyOrder);
//
//            childList.forEach(obj->{
//                ZyOrderItem item = new ZyOrderItem();
//                item.setId(UUIDUtil.generate());
//                item.setItemId(obj.getId());
//                item.setOrderId(zyOrder.getId());
//                item.setDrugId(obj.getZyDrugId());
//                item.setDrugName(obj.getDrugName());
//                item.setNum(obj.getNum());
//                item.setRetailPrice(obj.getRetailPrice());
//                item.setFee(obj.getFee());
//                zyOrderItemMapper.insert(item);
//
//            });
//        }
//
//        zhangYaoMapper.updateItemDismantleStatus(ids.split(","));

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
