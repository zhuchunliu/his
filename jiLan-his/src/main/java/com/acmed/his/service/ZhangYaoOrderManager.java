package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.ZhangYaoConstant;
import com.acmed.his.dao.*;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.ZyOrderItemHistoryDto;
import com.acmed.his.model.dto.ZyOrderItemUnpaidDto;
import com.acmed.his.model.dto.ZyOrderItemUnsubmitDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.zy.ZYAddressMo;
import com.acmed.his.pojo.zy.ZYHistoryQueryMo;
import com.acmed.his.pojo.zy.ZYOrderSubmitPayMo;
import com.acmed.his.pojo.zy.dto.ZYOrderGetObj;
import com.acmed.his.pojo.zy.dto.ZYOrderPostObj;
import com.acmed.his.pojo.zy.dto.ZYPayObj;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
    private ZhangYaoManager zhangYaoManager;

    @Autowired
    private ZyOrderMapper zyOrderMapper;

    @Autowired
    private ZyOrderItemMapper zyOrderItemMapper;

    @Autowired
    private CommonManager commonManager;

    @Autowired
    private ZyOrderFeedbackMapper zyOrderFeedbackMapper;

    @Autowired
    private ZyOrderItemFeedbackMapper zyOrderItemFeedbackMapper;

    @Autowired
    private ZyAddressFeedbackMapper zyAddressFeedbackMapper;

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
                    order.setRecepitStatus(-1);
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
                zhangYaoMapper.updatePreItemStatusByIds(itemIdList,1);

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
                zhangYaoMapper.updatePreItemStatusByOrderId(orderId,3);//设定t_b_prescription_item为取消状态
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

                zhangYaoMapper.updatePreItemStatusById(item.getPreItemId(),3);//设定t_b_prescription_item为取消状态
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
        orderItem.setStatus(0);
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
    public String submit(ZYOrderSubmitPayMo mo, UserInfo info) {


        StringBuilder builder = new StringBuilder();
        synchronized ((Object)info.getOrgCode().intValue()) {

            //step0:处理地址信息
            List<ZyAddress> addressList = zhangYaoManager.getAddressList(1,info);
            ZYAddressMo zyAddressMo = new ZYAddressMo();
            BeanUtils.copyProperties(mo,zyAddressMo,"id");
            zyAddressMo.setIsDefault(1);
            if(null != addressList){
                zyAddressMo.setId(addressList.get(0).getId());
            }
            ZyAddress zyAddress = zhangYaoManager.saveAddress(zyAddressMo,info);


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
            String groupNum = "GM_"+commonManager.getNextVal("zyGroupNum");
            String submitTime = LocalDateTime.now().toString();
            for(ZYOrderSubmitPayMo.ZYOrderSubmitPayDetailMo detailMo : mo.getDetailMoList()){
                List<ZyOrderItem> itemList = zyOrderItemMapper.getItemByOrderIdExclueRemove(detailMo.getOrderId());
                orderItemMap.put(detailMo.getOrderId(),itemList);

                List<String> itemIdList = Lists.newArrayList();
                itemList.forEach(obj->itemIdList.add(obj.getId()));

                ZyOrder zyOrder = orderMap.get(detailMo.getOrderId());
                BeanUtils.copyProperties(mo,zyOrder);//设置地址信息
                zyOrder.setAddressId(zyAddress.getId());

                zyOrder.setExpressId(detailMo.getExpressId());
                zyOrder.setExpressFee(detailMo.getExpressFee());
                zyOrder.setExpressName(detailMo.getExpressName());
                zyOrder.setSubmitTime(submitTime);
                zyOrder.setTotalFee(zyOrder.getDrugFee()+zyOrder.getExpressFee());
                zyOrder.setPayStatus(1);
                zyOrder.setGroupNum(groupNum);
                zyOrder.setModifyBy(info.getId().toString());
                zyOrder.setModifyAt(LocalDateTime.now().toString());
                zyOrderMapper.updateByPrimaryKey(zyOrder);

                if(!detailMo.getItemIdList().containsAll(itemIdList) || !itemIdList.containsAll(detailMo.getItemIdList())){
                    flag = false;
                    zyOrderItemMapper.updateAddStatus(zyOrder.getId(),detailMo.getItemIdList());//过滤掉当前的详情,设置成新增状态
                    zyOrderItemMapper.updateRemoveStatus(zyOrder.getId(),detailMo.getItemIdList());//过滤removed=1,设置成删除状态
                }
            }
            if(!flag){//保存成功，但是返回错误信息
                return String.valueOf(false);
            }

            //setp3:给掌药提交数据,并验证，验证成功保存数据库
            this.postRemoteOrder(orderMap,orderItemMap,info);

            //step4:支付
            return this.pay(mo.getFeeType(),orderMap);

        }
    }

    /**
     *
     * @param feeType 1:微信，2:支付宝
     * @param orderMap
     */
    private String pay(Integer feeType,Map<String, ZyOrder> orderMap) {

        List<String> list = Lists.newArrayList();
        Double totalFee = 0d;
        for(String orderId : orderMap.keySet()){
            list.add(orderMap.get(orderId).getZyOrderId());
            totalFee += orderMap.get(orderId).getTotalFee();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String,Object>();
        paramMap.add("orderIds", list.toArray());
        paramMap.add("payType",feeType);

        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(paramMap,httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> json = restTemplate.postForEntity("http://presapi.lkhealth.net/index.php"+ZhangYaoConstant.buildPayUrl(),httpEntity, JSONObject.class);

        if(200 != json.getStatusCodeValue()){
            logger.error("支付数据: "+paramMap);
            logger.error("支付返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败，请代会重试");
        }
        if(1 !=json.getBody().getInteger("code")){
            logger.error("支付数据: "+paramMap);
            logger.error("支付返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败，请代会重试");
        }
        ZYPayObj zyPayObj = json.getBody().getJSONObject("data").getObject("backInfo",ZYPayObj.class);
        if(zyPayObj.getPayPrice().doubleValue() != totalFee){
            logger.error("支付金额不匹配");
            logger.error("支付数据: "+paramMap);
            logger.error("支付返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败，请代会重试");
        }
        return zyPayObj.getCodeUrl();
    }

    /**
     * 给掌药下单
     * @param orderMap
     * @param orderItemMap
     */
    public void postRemoteOrder(Map<String,ZyOrder> orderMap,Map<String,List<ZyOrderItem>> orderItemMap,UserInfo info){
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

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String,Object>();
        JSONArray jsonArray = new JSONArray();
        jsonArray.addAll(objList);
        paramMap.add("dataList",jsonArray.toJSONString());

        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(paramMap,httpHeaders);

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

        JSONArray array = json.getBody().getJSONArray("data");
        Map<String,ZYOrderGetObj> orderGetObjMap = Maps.newHashMap();
        for(int index = 0; index < array.size(); index++){
            ZYOrderGetObj obj = array.getObject(index,ZYOrderGetObj.class);
            orderGetObjMap.put(obj.getOrderInfo().getStoreId(),obj);
        }
        //验证数据是否准确
        if(orderGetObjMap.size() != orderMap.size()){
            logger.error("下单数据: "+jsonArray.toJSONString());
            logger.error("返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败");
        }

        for(String orderId : orderMap.keySet()) {
            ZyOrder order = orderMap.get(orderId);
            ZYOrderGetObj getObj = orderGetObjMap.get(order.getZyStoreId());
            if(null == getObj || null == getObj.getAddressInfo()
                    || null == getObj.getOrderDetail() || 0 == getObj.getOrderDetail().size()){
                logger.error("下单数据: "+jsonArray.toJSONString());
                logger.error("下单返回值："+json);
                throw new BaseException(StatusCode.FAIL,"下单失败");
            }
            //细节比较暂时不做，只做金额验证，直接记录数据库
            if(Double.parseDouble(getObj.getOrderInfo().getOrderAmount()) != order.getTotalFee().doubleValue()){
                logger.error("下单数据: "+jsonArray.toJSONString());
                logger.error("下单返回值："+json);
                logger.error("金额计算有误");
                throw new BaseException(StatusCode.FAIL,"下单失败");
            }

            order.setZyOrderId(getObj.getOrderInfo().getOrderId());
            order.setZyOrderSn(getObj.getOrderInfo().getOrderSn());
            order.setPayStatus(1);
            zyOrderMapper.updateByPrimaryKey(order);

            ZyOrderFeedback zyOrderFeedback = new ZyOrderFeedback();
            BeanUtils.copyProperties(getObj.getOrderInfo(),zyOrderFeedback);
            zyOrderFeedback.setCreateAt(LocalDateTime.now().toString());
            zyOrderFeedback.setCreateBy(info.getId().toString());
            zyOrderFeedbackMapper.insert(zyOrderFeedback);

            ZyAddressFeedback zyAddressFeedback = new ZyAddressFeedback();
            BeanUtils.copyProperties(getObj.getAddressInfo(),zyAddressFeedback);
            zyAddressFeedback.setCreateAt(LocalDateTime.now().toString());
            zyAddressFeedback.setCreateBy(info.getId().toString());
            zyAddressFeedbackMapper.insert(zyAddressFeedback);

            for(int index =0; index < getObj.getOrderDetail().size(); index++){
                ZyOrderItemFeedback zyOrderItemFeedback = new ZyOrderItemFeedback();
                BeanUtils.copyProperties(getObj.getOrderDetail().get(index),zyOrderItemFeedback);
                zyOrderItemFeedback.setCreateAt(LocalDateTime.now().toString());
                zyOrderItemFeedback.setCreateBy(info.getId().toString());
                zyOrderItemFeedbackMapper.insert(zyOrderItemFeedback);
            }
        }


    }

    public static void main(String[] args) {
        HttpHeaders httpHeaders = new HttpHeaders();

        MultiValueMap<String, Object> paramMap = new LinkedMultiValueMap<String,Object>();
        List<String> list = Lists.newArrayList("193202","193203");
        paramMap.add("orderIds", list.toArray());
        paramMap.add("payType","2");

        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<MultiValueMap<String,Object>>(paramMap,httpHeaders);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> json = restTemplate.postForEntity("http://presapi.lkhealth.net/index.php"+ZhangYaoConstant.buildPayUrl(),httpEntity, JSONObject.class);
        if(200 != json.getStatusCodeValue()){
//            logger.error("支付数据: "+paramMap);
//            logger.error("支付返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败，请代会重试");
        }
        if(1 !=json.getBody().getInteger("code")){
//            logger.error("支付数据: "+paramMap);
//            logger.error("支付返回值："+json);
            throw new BaseException(StatusCode.FAIL,"下单失败，请代会重试");
        }
        ZYPayObj zyPayObj = json.getBody().getJSONObject("data").getObject("backInfo",ZYPayObj.class);
        System.err.println(zyPayObj);
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
     * 获取待支付列表
     * @param user
     * @param name
     * @return
     */
    public List<ZyOrderItemUnpaidDto> getUnpaidDetail(UserInfo user, String name) {

        return zhangYaoMapper.getUnpaidDetail(user.getOrgCode(),name);
    }


    /**
     * 删除待支付详情
     * @param user
     * @param orderId
     * @param itemId
     */
    public void delUnpaidOrder(UserInfo user, String orderId, String itemId) {
//        return zhangYaoMapper.getUnpaidOrder(user.getOrgCode(),name);
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
