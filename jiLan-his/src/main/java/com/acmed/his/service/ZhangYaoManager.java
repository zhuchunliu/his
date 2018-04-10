package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.ZhangYaoConstant;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.zhangyao.ZYDrug;
import com.acmed.his.pojo.mo.DrugZYQueryMo;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class ZhangYaoManager {

    private Logger logger = LoggerFactory.getLogger(ZhangYaoManager.class);

    @Autowired
    private Environment environment;



    /**
     * 获取药品信息
     * @return
     */
    public PageResult<ZYDrug> getDrugList(PageBase<DrugZYQueryMo> pageBase) {
        StringBuilder builder = new StringBuilder(environment.getProperty("zhangyao.url"));
        DrugZYQueryMo mo = Optional.ofNullable(pageBase.getParam()).orElse(new DrugZYQueryMo());
        builder.append(ZhangYaoConstant.buildDrugUrl(mo.getName(),pageBase.getPageNum(),pageBase.getPageSize(),3,
                mo.getLat(),mo.getLng(),null));
        RestTemplate restTemplate = new RestTemplate();
        JSONObject json = restTemplate.getForObject(builder.toString(), JSONObject.class);
        if(json.get("code").equals("1")){
            PageResult result = new PageResult();
            result.setTotal(json.getJSONObject("data").getLong("totalCount"));
            JSONArray jsonArray = json.getJSONObject("data").getJSONArray("bigSearchList");
            List<ZYDrug> drugList = Lists.newArrayList();
            for(int i = 0;i<jsonArray.size();i++){
                ZYDrug zyDrug = JSONObject.parseObject(jsonArray.getString(1),ZYDrug.class);
                drugList.add(zyDrug);
            }
            result.setData(drugList);
            return result;
        }else{
            logger.error("get zhangyao drug fail,msg: "+json.get("message")+"  ;the url is :"+builder.toString());
            throw new BaseException(StatusCode.FAIL,"获取掌药药品信息失败");
        }

    }
}
