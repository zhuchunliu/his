package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.ZhangYaoConstant;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.pojo.zy.ZYDrugDetailVo;
import com.acmed.his.pojo.zy.ZYDrugListVo;
import com.acmed.his.pojo.zy.dto.*;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2018-04-10
 **/
@Service
public class ZhangYaoManager implements InitializingBean {

    private Logger logger = LoggerFactory.getLogger(ZhangYaoManager.class);

    @Autowired
    private Environment environment;

    private static String ZHANGYAO_URL = "zhangyao.url";

    @Override
    public void afterPropertiesSet() throws Exception {
        this.ZHANGYAO_URL = environment.getProperty("zhangyao.url");
    }
    /**
     * 获取药品信息
     * @return
     */
    public PageResult<ZYDrugListVo> getDrugList(PageBase<DrugZYQueryMo> pageBase) {
        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL);
        DrugZYQueryMo mo = Optional.ofNullable(pageBase.getParam()).orElse(new DrugZYQueryMo());
        builder.append(ZhangYaoConstant.buildDrugListUrl(mo.getName(),(pageBase.getPageNum()-1)*pageBase.getPageSize(),pageBase.getPageSize(),3,
                mo.getLat(),mo.getLng(),null));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            PageResult result = new PageResult();
            result.setTotal(json.getJSONObject("data").getLong("totalCount"));
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("bigSearchList");
            List<ZYDrugListVo> drugList = Lists.newArrayList();
            for(int i = 0;i<jsonArray.size();i++){
                ZYDrugListObj zyDrug = JSONObject.parseObject(jsonArray.getString(i),ZYDrugListObj.class);
                ZYDrugListVo vo = new ZYDrugListVo();
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

            ZYDrugDetailVo.ZYDrugStoreDetailVo detailVo = new ZYDrugDetailVo.ZYDrugStoreDetailVo();
            BeanUtils.copyProperties(detailObj,detailVo);
            vo.setDrugStoreDetailVo(detailVo);

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
     * @param orderSn
     * @return
     */
    public ZYLogisticsObj getLogistics(String orderSn) {
        StringBuilder builder = new StringBuilder(this.ZHANGYAO_URL).append(ZhangYaoConstant.buildLogisticsUrl(orderSn));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            ZYLogisticsObj obj = JSONObject.parseObject(json.getJSONObject("data").getJSONObject("logisticsInfo").toJSONString(),ZYLogisticsObj.class);
            List<ZYLogisticsObj.ZYLogisticsDetailObj> list = JSONArray.parseArray(json.getJSONObject("data").getJSONObject("logisticsInfo").
                    getJSONArray("processList").toJSONString(),ZYLogisticsObj.ZYLogisticsDetailObj.class);
            obj.setDetailObjList(list);
            return obj;
        }else{
            logger.error("get zhangyao logistics fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取物流信息失败");
        }
    }
}
