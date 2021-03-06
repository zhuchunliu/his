package com.acmed.his.service;


import com.acmed.his.constants.RedisKeyConstants;
import com.acmed.his.model.OpenIdAndAccessToken;
import com.acmed.his.pojo.mo.WxUserInfo;
import com.acmed.his.util.EmojiUtil;
import com.acmed.his.util.RandomUtil;
import com.alibaba.fastjson.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.TimeUnit;


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

    @Autowired
    @Qualifier(value="stringRedisTemplate")
    private RedisTemplate redisTemplate;
    /**
     * 获取opendid
     * @param code code
     * @return openid
     */
    public OpenIdAndAccessToken getOpenid(String code) throws Exception{
        String url =  String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                environment.getProperty("weixin.appid"),environment.getProperty("weixin.secret"),code);
        String info = new RestTemplate().getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(info);
        String access_token = json.getString("access_token");
        String openid = json.getString("openid");
        if (StringUtils.isEmpty(access_token) || StringUtils.isEmpty(openid)){
            logger.error("微信获取openid异常: "+json.toJSONString());
        }
        OpenIdAndAccessToken openIdAndAccessToken = new OpenIdAndAccessToken();
        openIdAndAccessToken.setOpenId(openid);
        openIdAndAccessToken.setAccessToken(access_token);
        return openIdAndAccessToken;
    }

    /**
     * 获取基础token
     * @return
     */
    public String getBaseAccessToken(){
        String code = Optional.ofNullable(redisTemplate.opsForValue().get(RedisKeyConstants.WX_BASE_ACCESS_TOKEN)).map(obj->obj.toString()).
                orElse(null);
        if (StringUtils.isNotEmpty(code)){
            return code;
        }
        String url =  String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                environment.getProperty("weixin.appid"),environment.getProperty("weixin.secret"));
        String info = new RestTemplate().getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(info);
        String accessToken = json.getString("access_token");
        redisTemplate.opsForValue().set(RedisKeyConstants.WX_BASE_ACCESS_TOKEN,accessToken);
        redisTemplate.expire(RedisKeyConstants.WX_BASE_ACCESS_TOKEN,100, TimeUnit.MINUTES);
        return accessToken;
    }


    public WxUserInfo wxUserInfo(String openid,String accessToken) throws IOException {

        logger.error("openid "+openid+" accessToken "+accessToken);

        String url = null;
        if(environment.getActiveProfiles()[0].equals("pre")){//预发
            url = String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN",
                    accessToken, openid);
        }else{
            String accessTokenUrl =  String.format("https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s",
                environment.getProperty("weixin.appid"),environment.getProperty("weixin.secret"));
            String info = new RestTemplate().getForObject(accessTokenUrl, String.class);
            JSONObject jsonobject = JSONObject.parseObject(info);
            accessToken = jsonobject.getString("access_token");
            url = String.format("https://api.weixin.qq.com/cgi-bin/user/info?access_token=%s&openid=%s&lang=zh_CN", accessToken, openid);
        }
        URL url1 = new URL(url);
        HttpURLConnection urlConnection = (HttpURLConnection)url1.openConnection();

        InputStream inputStream = urlConnection.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream,"UTF-8");
        BufferedReader in = new BufferedReader(inputStreamReader);

        String jsonUserStr =in.readLine().toString();

// 释放资源
        inputStream.close();
        urlConnection.disconnect();
        JSONObject json = JSONObject.parseObject(jsonUserStr);
        WxUserInfo wxUserInfo = new WxUserInfo();
        wxUserInfo.setHeadImgUrl(json.getString("headimgurl"));
        wxUserInfo.setNickName(StringUtils.isEmpty(json.getString("nickname"))?null:EmojiUtil.emojiConvert(json.getString("nickname")));
        String sex = json.getString("sex");
        if (StringUtils.equals("2",sex)){
            wxUserInfo.setSex("1");
        }else if (StringUtils.equals("1",sex)){
            wxUserInfo.setSex("0");
        }else {
            wxUserInfo.setSex(null);
        }
        return wxUserInfo;
    }
}
