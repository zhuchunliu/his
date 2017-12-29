package com.acmed.his.service;


import com.alibaba.fastjson.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


/**
 * WxManager
 * 微信相关的
 * @author jimson
 * @date 2017/11/22
 */
@Service
public class WxManager {

    private Logger logger = LoggerFactory.getLogger(WxManager.class);

    @Autowired
    private Environment environment;

    /**
     * 获取opendid
     * @param code code
     * @return openid
     */
    public String getOpenid(String code) throws Exception{
        String url =  String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                environment.getProperty("weixin.appid"),environment.getProperty("weixin.secret"),code);
        String info = new RestTemplate().getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(info);
        logger.info("微信获取openid: "+json.toJSONString());
        return json.getString("openid");
    }




}
