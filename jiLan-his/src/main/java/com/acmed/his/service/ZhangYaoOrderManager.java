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
import com.acmed.his.model.dto.ZyOrderItemHistoryDto;
import com.acmed.his.model.dto.ZyOrderItemUnpaidDto;
import com.acmed.his.model.dto.ZyOrderItemUnsubmitDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.zy.*;
import com.acmed.his.pojo.zy.dto.ZYOrderPostObj;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.UUIDUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
    public List<ZyOrderItemUnsubmitDto> getUnSubmitOrder(UserInfo info, String name) {

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
                    order.setTotalFee(order.getDrugFee()+order.getExpressFee());
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
                    order.setTotalFee(order.getDrugFee()+order.getExpressFee());
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
                zyOrder.setDrugFee(0d);
                zyOrder.setTotalFee(0d);
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
                zyOrder.setDrugFee(zyOrder.getDrugFee()-item.getFee());
                zyOrder.setTotalFee(zyOrder.getTotalFee()-item.getFee());
                zyOrderMapper.updateByPrimaryKey(zyOrder);
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
                zyOrder.setExpressName(detailMo.getExpressName());
                zyOrder.setSubmitTime(LocalDateTime.now().toString());
                zyOrder.setTotalFee(zyOrder.getDrugFee()+zyOrder.getExpressFee());
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
            this.postRemoteOrder(orderMap,orderItemMap);

        }
        return null;
    }

    /**
     * 给掌药下单
     * @param orderMap
     * @param orderItemMap
     */
    public void postRemoteOrder(Map<String,ZyOrder> orderMap,Map<String,List<ZyOrderItem>> orderItemMap){
        List<ZYOrderPostObj> objList = Lists.newArrayList();
        for(String orderId : orderMap.keySet()){
            ZyOrder order = orderMap.get(orderId);
            ZYOrderPostObj obj = new ZYOrderPostObj();
            obj.setStoreId(order.getZyStoreId());
            obj.setDeliverId("45");
            obj.setTotal(order.getTotalFee().toString());
            obj.setExpId(order.getExpressId());
            obj.setProvinceId(order.getProvinceId());
            obj.setCityId(order.getCityId());
            obj.setAreaId(order.getCountyId());
            obj.setAreaInfo(order.getProvinceName()+"-"+order.getCityName()+"-"+order.getCountyName());
            obj.setAddress(order.getAddress());
            obj.setTrueName(order.getRecipient());
            obj.setMobPhone(order.getPhone());
            List<ZYOrderPostObj.ZYOrderPostDetailObj> drugList =Lists.newArrayList();

            for(ZyOrderItem orderItem : orderItemMap.get(orderId)){
                ZYOrderPostObj.ZYOrderPostDetailObj detailObj = new ZYOrderPostObj.ZYOrderPostDetailObj();
                detailObj.setDrugId(orderItem.getDrugId());
                detailObj.setDrugNum(orderItem.getNum());
                drugList.add(detailObj);
            }
            obj.setDrugList(drugList);
            objList.add(obj);
        }

        HttpHeaders httpHeaders = new HttpHeaders();

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,Object>();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(objList);
        map.add("dataList",jsonArray.toJSONString());

        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(map,httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> json = restTemplate.postForEntity(this.ZHANGYAO_URL+ZhangYaoConstant.buildOrderUrl(),httpEntity, JSONObject.class);

        if(200 != json.getStatusCodeValue()){
            logger.error("下单数据: "+jsonArray.toJSONString());
            logger.error("返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败，请代会重试");
        }
        if(1 !=json.getBody().getInteger("code")){
            logger.error("下单数据: "+jsonArray.toJSONString());
            logger.error("返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败，请代会重试");
        }
    }

    public static void main(String[] args) {
        String json = "[{\"address\":\"中节能\",\"areaId\":\"320586\",\"areaInfo\":\"江苏省-苏州市-工业园区\",\"cityId\":\"320500\",\"deliverId\":\"45\",\"drugList\":[{\"drugId\":214459,\"drugNum\":5},{\"drugId\":214459,\"drugNum\":3}],\"expId\":\"418\",\"mobPhone\":\"119\",\"provinceId\":\"320000\",\"storeId\":\"349671\",\"total\":\"248.0\",\"trueName\":\"张三林\"},{\"address\":\"中节能\",\"areaId\":\"320586\",\"areaInfo\":\"江苏省-苏州市-工业园区\",\"cityId\":\"320500\",\"deliverId\":\"45\",\"drugList\":[{\"drugId\":7343,\"drugNum\":3},{\"drugId\":7343,\"drugNum\":5}],\"expId\":\"3512\",\"mobPhone\":\"119\",\"provinceId\":\"320000\",\"storeId\":\"349996\",\"total\":\"125.0\",\"trueName\":\"张三林\"}]";
        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        MultiValueMap<String, Object> map = new LinkedMultiValueMap<String,Object>();
        map.add("dataList",json);

        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(map,httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> result = restTemplate.postForEntity("http://presapi.lkhealth.net/index.php?r=jizhi/order/place"
                ,httpEntity, JSONObject.class);
        System.err.println(result);
    }




    /**
     * 获取待支付列表
     * @param user
     * @param name
     * @return
     */
    public List<ZyOrderItemUnpaidDto> getUnpaidOrder(UserInfo user, String name) {

        return zhangYaoMapper.getUnpaidOrder(user.getOrgCode(),name);
    }


    /**
     * 删除待支付详情
     * @param user
     * @param orderId
     * @param itemId
     */
    public void delUnpaidOrder(UserInfo user, String orderId, String itemId) {
    }


    /**
     * 获取历史订单
     * @param user
     * @param mo
     * @return
     */
    public PageResult<ZyOrderItemHistoryDto> getHistoryOrder(UserInfo user, PageBase<ZYHistoryQueryMo> mo) {
        Page page = PageHelper.startPage(mo.getPageNum(),mo.getPageSize());
        List<ZyOrderItemHistoryDto> list = zhangYaoMapper.getHistoryOrder(user.getOrgCode(), Optional.ofNullable(mo.getParam()).orElse(new ZYHistoryQueryMo()));
        PageResult<ZyOrderItemHistoryDto> result = new PageResult<>();
        result.setData(list);
        result.setTotal(page.getTotal());
        return result;
    }

    /**
     * 删除历史订单
     * @param user
     * @param orderId
     */
    public void delHistoryOrder(UserInfo user, String orderId) {
        ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(orderId);
        zyOrder.setPayStatus(4);
        zyOrder.setModifyAt(LocalDateTime.now().toString());
        zyOrder.setModifyBy(user.getId().toString());
        zyOrderMapper.updateByPrimaryKey(zyOrder);
    }





}
