package com.acmed.his.api;

import com.acmed.his.HisApplication;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.AccompanyingOrder;
import com.acmed.his.model.Patient;
import com.acmed.his.service.AccompanyingOrderManager;
import com.acmed.his.service.PatientManager;
import com.acmed.his.util.*;
import com.alibaba.druid.support.json.JSONUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * PayApiTest
 *
 * @author jimson
 * @date 2017/12/28
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class PayApiTest {

    @Autowired
    private Environment environment;


    @Test
    public void payBSGInit1() throws Exception {

        BigDecimal totalBalance = new BigDecimal(2);
        String openid = "oTAaixA9X74whvto1wK4H-zn9wAY";
        String mchId = environment.getProperty("weixin.mchId");
        Map<String,String> param = new HashMap<>(15);
        param.put("appid",environment.getProperty("weixin.appid"));
        param.put("mch_id",mchId);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign_type", WXPayConstants.MD5);
        param.put("body","预约挂号");
        //param.put("detail","详情");
        long currentTimestamp = WXPayUtil.getCurrentTimestamp();
        System.err.println(currentTimestamp);
        param.put("out_trade_no","2017122700001000540015004731");
        param.put("total_fee", "2");
        // 客户端ip
        param.put("spbill_create_ip","127.0.0.1");
        param.put("notify_url",environment.getProperty("weixin.url")+"/pay/BSGCallBack");
        param.put("trade_type","JSAPI");
        param.put("openid",openid);
        // 生成签名
        String s = WXPayUtil.generateSignature(param, environment.getProperty("weixin.key"));
        param.put("sign",s);
        String xml = WXPayUtil.mapToXml(param);
        String s1 = WXPayRequest.postXml(WXPayConstants.UNIFIEDORDER_URL_SUFFIX,xml);
        Map<String, String> map = WXPayUtil.xmlToMap(s1);

        if (map.get("result_code").equals("SUCCESS")){
            Map<String,String> result = new HashMap<>();
            result.put("appId",map.get("appid"));
            result.put("timeStamp",WXPayUtil.getCurrentTimestamp()+"");
            result.put("nonceStr", UUIDUtil.generate32());
            result.put("package","prepay_id="+map.get("prepay_id"));
            result.put("signType","MD5");
            String s2 = WXPayUtil.generateSignature(result, environment.getProperty("weixin.key"));
            result.put("paySign",s2);

            System.err.println(JSONUtils.toJSONString(result));
        }
    }

    @Test
    public void BSGCallBack() throws Exception {
    }

    /**
     * 查询订单
     * @throws Exception
     */
    @Test
    public void orderquery1() throws Exception {
        String mchId = environment.getProperty("weixin.mchId");
        Map<String,String> param = new HashMap<>(15);
        param.put("appid",environment.getProperty("weixin.appid"));
        param.put("mch_id",mchId);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign_type", WXPayConstants.MD5);
        param.put("out_trade_no","2017122700001000540015004731");
        String s = WXPayUtil.generateSignature(param, environment.getProperty("weixin.key"));
        param.put("sign",s);
        String xml = WXPayUtil.mapToXml(param);
        String s1 = WXPayRequest.postXml(WXPayConstants.ORDERQUERY_URL_SUFFIX,xml);
        Map<String, String> map = WXPayUtil.xmlToMap(s1);
        System.err.println(map);
    }



}