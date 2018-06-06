package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.ZhangYaoConstant;
import com.acmed.his.dao.ZhangYaoMapper;
import com.acmed.his.dao.ZyAddressMapper;
import com.acmed.his.dao.ZyOrderMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.ZyAddress;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.zy.ZYDrugDetailVo;
import com.acmed.his.pojo.zy.ZYDrugListVo;
import com.acmed.his.pojo.zy.ZYAddressMo;
import com.acmed.his.pojo.zy.ZYLogisticsVo;
import com.acmed.his.pojo.zy.dto.*;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-04-10
 **/
@Service
public class ZhangYaoManager implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(ZhangYaoManager.class);

    @Autowired
    private Environment environment;

    @Autowired
    private ZhangYaoMapper zhangYaoMapper;

    @Autowired
    private ZyAddressMapper addressMapper;

    @Autowired
    private ZyOrderMapper zyOrderMapper;

    private static String ZHANGYAO_URL = null;

    @Override
    public void afterPropertiesSet() throws Exception {
        this.ZHANGYAO_URL = environment.getProperty("zhangyao.url");
    }
    /**
     * 获取药品信息
     * @return
     */
    public PageResult<ZYDrugListVo> getDrugList(PageBase<DrugZYQueryMo> pageBase,UserInfo info) {
        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL);
        DrugZYQueryMo mo = Optional.ofNullable(pageBase.getParam()).orElse(new DrugZYQueryMo());
        builder.append(ZhangYaoConstant.buildDrugListUrl(mo.getName(),(pageBase.getPageNum()-1)*pageBase.getPageSize(),pageBase.getPageSize(),mo.getType(),
                mo.getLat(),mo.getLng(),null));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            PageResult result = new PageResult();
            result.setTotal(json.getJSONObject("data").getLong("totalCount"));
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("bigSearchList");
            List<ZYDrugListVo> drugList = Lists.newArrayList();
            List<Integer> drugIdList = Lists.newArrayList();
            for(int i = 0;i<jsonArray.size();i++){
                ZYDrugListObj zyDrug = JSONObject.parseObject(jsonArray.getString(i),ZYDrugListObj.class);
                ZYDrugListVo vo = new ZYDrugListVo();
                BeanUtils.copyProperties(zyDrug,vo);
                vo.setSpec(zyDrug.getForm());
                vo.setRetailPrice(zyDrug.getGoodsPrice());
                vo.setManufacturerName(zyDrug.getCompanyName());
                vo.setDrugName(zyDrug.getCnName());
                drugIdList.add(vo.getDrugId());
                drugList.add(vo);
            }

            //获取每个药被使用次数
            List<Map<String,Object>> useList = zhangYaoMapper.statisDrugUsed(drugIdList,info.getOrgCode());
            Map<String,Integer> useMap = Maps.newHashMap();
            for(Map<String,Object> map : useList){
                useMap.put(map.get("zydrugid").toString(),
                        Optional.ofNullable(map.get("num")).map(String::valueOf).map(Integer::parseInt).orElse(0));
            }

            drugList.forEach(obj->obj.setUseNum(useMap.get(obj.getDrugId().toString())));

            result.setData(drugList);
            return result;
        }else{
            logger.error("get zhangyao drug fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取掌药药品信息失败");
        }

    }

    /**
     * 获取药品详情
     * @param storeId 店铺id
     * @param drugId 药品id
     * @return
     */
    public ZYDrugDetailVo getDrugDetail(String storeId, String drugId) {

        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL).append(ZhangYaoConstant.buildDrugDetailUrl(storeId,drugId));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            ZYDrugDetailObj obj = JSONObject.parseObject(json.getJSONObject("data").getJSONObject("drugInfo").toJSONString(),ZYDrugDetailObj.class);
            List<String> drugPicList = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("drugPicList").toJSONString(),String.class);
            ZYDrugDetailObj.ZYDrugStoreDetailObj detailObj = JSONObject.parseObject(json.getJSONObject("data").getJSONObject("storeDetail").toJSONString(),ZYDrugDetailObj.ZYDrugStoreDetailObj.class);

            ZYDrugDetailVo vo = new ZYDrugDetailVo();
            BeanUtils.copyProperties(obj,vo);
            vo.setDrugPicList(drugPicList);

            vo.setSpec(obj.getForm());
            vo.setApprovalNumber(obj.getNumber());
            vo.setRetailPrice(obj.getGoodsPrice());
            vo.setManufacturerName(obj.getCompanyName());

            vo.setCity(null != detailObj?detailObj.getCity():"");
            return vo;
        }else{
            logger.error("get zhangyao drug detail fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取掌药药品详情信息失败");
        }
    }

    /**
     * 获取药店详情
     * @param storeId
     * @return
     */
    public ZYStoreDetailObj getStoreDetail(String storeId) {
        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL).append(ZhangYaoConstant.buildStoreDetailUrl(storeId));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            ZYStoreDetailObj obj = JSONObject.parseObject(json.getJSONObject("data").getJSONObject("storeDetail").toJSONString(),ZYStoreDetailObj.class);
            return obj;
        }else{
            logger.error("get zhangyao store detail fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取掌药药店详情信息失败");
        }


    }




    /**
     * 获取地址列表信息
     * @param areaId
     * @return
     */
    public List<ZYCityObj> getCity(String areaId) {
        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL).append(ZhangYaoConstant.buildCityUrl(areaId));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            List<ZYCityObj> list = JSONArray.parseArray(json.getJSONArray("data").toJSONString(),ZYCityObj.class);
            return list;
        }else{
            logger.error("get zhangyao city fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取地址信息失败");
        }
    }


    /**
     * 获取快递信息
     * @param storeId 店铺id
     * @param provinceId 省份id
     * @return
     */
    public List<ZYExpressObj> getExpress(String storeId, String provinceId) {
        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL).append(ZhangYaoConstant.buildExpressUrl(storeId,provinceId));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            List<ZYExpressObj> list = JSONArray.parseArray(json.getJSONObject("data").getJSONArray("expressList").toJSONString(),ZYExpressObj.class);
            return list;
        }else{
            logger.error("get zhangyao express fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取快递信息失败");
        }
    }

    /**
     * 获取物流信息
     * @param orderId 订单主键
     * @return
     */
    public ZYLogisticsVo getLogistics(String orderId) {
        ZyOrder zyOrder = zyOrderMapper.selectByPrimaryKey(orderId);

        ZYLogisticsVo vo = new ZYLogisticsVo();
        BeanUtils.copyProperties(zyOrder,vo);

        if(StringUtils.isEmpty(zyOrder.getZyOrderSn())){
            return vo;
        }
        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL).append(ZhangYaoConstant.buildLogisticsUrl(zyOrder.getZyOrderSn()));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            ZYLogisticsObj obj = JSONObject.parseObject(json.getJSONObject("data").getJSONObject("logisticsInfo").toJSONString(),ZYLogisticsObj.class);
            List<ZYLogisticsObj.ZYLogisticsDetailObj> list = JSONArray.parseArray(json.getJSONObject("data").getJSONObject("logisticsInfo").
                    getJSONArray("processList").toJSONString(),ZYLogisticsObj.ZYLogisticsDetailObj.class);
            obj.setDetailObjList(list);
            BeanUtils.copyProperties(obj,vo);
            return vo;
        }else{
            logger.error("get zhangyao logistics fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取物流信息失败");
        }
    }

    @Transactional
    public ZyAddress saveAddress(ZYAddressMo mo, UserInfo user) {

        if(1 == mo.getIsDefault()){//设为默认地址
            addressMapper.cancelAllDefault(user.getOrgCode());
        }

        ZyAddress zyAddress = null;
        if(null == mo.getId()){
            zyAddress = new ZyAddress();
            BeanUtils.copyProperties(mo,zyAddress);
            zyAddress.setOrgCode(user.getOrgCode());
            zyAddress.setRemoved("0");
            zyAddress.setCreateBy(user.getId().toString());
            zyAddress.setCreateAt(LocalDateTime.now().toString());
            addressMapper.insert(zyAddress);

        }else{
            zyAddress = addressMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,zyAddress);
            zyAddress.setModifyBy(user.getId().toString());
            zyAddress.setModifyAt(LocalDateTime.now().toString());
            addressMapper.updateByPrimaryKey(zyAddress);
        }
        return zyAddress;
    }

    /**
     * 删除地址信息
     * @param id
     * @return
     */
    public void delAddress(Integer id, UserInfo user) {
        ZyAddress zyAddress = addressMapper.selectByPrimaryKey(id);
        zyAddress.setModifyBy(user.getId().toString());
        zyAddress.setModifyAt(LocalDateTime.now().toString());
        zyAddress.setRemoved("1");
        addressMapper.updateByPrimaryKey(zyAddress);
    }

    /**
     * 设置默认的地址信息
     * @param id
     * @param user
     */
    @Transactional
    public void setDefaultAddress(Integer id, UserInfo user) {
        addressMapper.cancelAllDefault(user.getOrgCode());
        ZyAddress zyAddress = addressMapper.selectByPrimaryKey(id);
        zyAddress.setModifyBy(user.getId().toString());
        zyAddress.setModifyAt(LocalDateTime.now().toString());
        zyAddress.setIsDefault(1);
        addressMapper.updateByPrimaryKey(zyAddress);
    }

    public List<ZyAddress> getAddressList(Integer isDefault, UserInfo user) {
        Example example = new Example(ZyAddress.class);
//        example.excludeProperties("orgCode","removed","createAt","modifyAt","createBy","modifyBy");
        Example.Criteria criteria = example.createCriteria().andEqualTo("removed","0").
                andEqualTo("orgCode",user.getOrgCode());
        if(null != isDefault && 1 == isDefault){
            criteria.andEqualTo("isDefault","1");
        }
        example.orderBy("isDefault").desc().orderBy("createAt").desc();
        return addressMapper.selectByExample(example);
    }
}
