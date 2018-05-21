package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.ZhangYaoConstant;
import com.acmed.his.dao.ZhangYaoMapper;
import com.acmed.his.dao.ZyOrderItemMapper;
import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.zy.OrderItemDrugDto;
import com.acmed.his.model.zy.ZyOrder;
import com.acmed.his.model.zy.ZyOrderItem;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.zy.*;
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

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-04-10
 **/
@Service
public class ZhangYaoManager {

    private Logger logger = LoggerFactory.getLogger(ZhangYaoManager.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ZhangYaoMapper zhangYaoMapper;

    @Autowired
    private ZyOrderMapper zyOrderMapper;

    @Autowired
    private ZyOrderItemMapper zyOrderItemMapper;

    /**
     * 获取药品信息
     * @return
     */
    public PageResult<ZYDrugVo> getDrugList(PageBase<DrugZYQueryMo> pageBase) {
        StringBuilder builder = new StringBuilder(environment.getProperty("zhangyao.url"));
        DrugZYQueryMo mo = Optional.ofNullable(pageBase.getParam()).orElse(new DrugZYQueryMo());
        builder.append(ZhangYaoConstant.buildDrugUrl(mo.getName(),(pageBase.getPageNum()-1)*pageBase.getPageSize(),pageBase.getPageSize(),3,
                mo.getLat(),mo.getLng(),null));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            PageResult result = new PageResult();
            result.setTotal(json.getJSONObject("data").getLong("totalCount"));
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("bigSearchList");
            List<ZYDrugVo> drugList = Lists.newArrayList();
            for(int i = 0;i<jsonArray.size();i++){
                ZYDrugObj zyDrug = JSONObject.parseObject(jsonArray.getString(i),ZYDrugObj.class);
                ZYDrugVo vo = new ZYDrugVo();
                BeanUtils.copyProperties(zyDrug,vo);
                vo.setSpec(zyDrug.getForm());
                vo.setRetailPrice(zyDrug.getGoodsPrice());
                vo.setNum(zyDrug.getStorage());
                vo.setManufacturerName(zyDrug.getCompanyName());
                vo.setDrugName(zyDrug.getCnName());
                drugList.add(vo);
            }
            result.setData(drugList);
            return result;
        }else{
            logger.error("get zhangyao drug fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取掌药药品信息失败");
        }

    }

    public PageResult<ZYDrugVo> getDrugDetail(String storeId, String drugId) {
        return null;
    }

    /**
     * 获取未下单的处方
     * @param orgCode
     */
    public List<OrderItemDrugDto> getUnOrder(Integer orgCode) {
        return zhangYaoMapper.getUnOrder(orgCode);
    }

    /**
     * 下单
     * @param ids
     */
    @Transactional
    public void order(String ids, UserInfo info) {
        List<PrescriptionItem> itemList = zhangYaoMapper.getPreItemByIds(ids.split(","));
        Map<String,List<PrescriptionItem>> map = Maps.newHashMap();
        itemList.forEach(obj->{
            if(map.containsKey(obj.getZyStoreId())){
                map.get(obj.getZyStoreId()).add(obj);
            }else{
                map.put(obj.getZyStoreId(),Lists.newArrayList(obj));
            }
        });



        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            List<PrescriptionItem> childList = map.get(iterator.next());
            ZyOrder zyOrder = new ZyOrder();
            zyOrder.setId(UUIDUtil.generate());
            zyOrder.setOrgCode(info.getOrgCode());
            zyOrder.setZyStoreId(childList.get(0).getZyStoreId());
            zyOrder.setZyStoreName(childList.get(0).getZyStoreName());
            zyOrder.setPayStatus(0);
            zyOrder.setIsRecepit(0);
            zyOrder.setCreateAt(LocalDateTime.now().toString());
            zyOrder.setCreateBy(info.getId().toString());

            zyOrder.setZyOrderId("191882");//暂时写死，用于测试
            zyOrder.setZyOrderSn("10102018032350539897");//暂时写死，用于测试
            zyOrderMapper.insert(zyOrder);

            childList.forEach(obj->{
                ZyOrderItem item = new ZyOrderItem();
                item.setId(UUIDUtil.generate());
                item.setItemId(obj.getId());
                item.setOrderId(zyOrder.getId());
                zyOrderItemMapper.insert(item);
            });
        }

        zhangYaoMapper.updateItemOrderStatus(ids.split(","));

        //TODO 掌药下单待开发

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
     * 订单处方详情
     * @param orderId
     */
    public List<OrderItemDrugDto> getOrderItemList(String orderId) {
        return zhangYaoMapper.getOrderItemList(orderId);
    }

    /**
     * 获取掌药订单详情
     * @param orderId
     */
    public ZYOrderDetailVo getOrderDetail(String orderId) {
        ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(orderId);
        if (null != zyOrder && StringUtils.isNotEmpty(zyOrder.getZyOrderSn())) {
            StringBuilder builder = new StringBuilder(environment.getProperty("zhangyao.url"));
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
