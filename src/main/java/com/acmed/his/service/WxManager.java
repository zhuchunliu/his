package com.acmed.his.service;

import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxJsapiConfig;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * WxManager
 * 微信相关的
 * @author jimson
 * @date 2017/11/22
 */
@Service
public class WxManager {
    /**
     * 实例化 统一业务API入口
     */
    private IService wxService = new WxService();

    private Logger logger = Logger.getLogger(WxManager.class);

    @Autowired
    private Environment environment;

    /**
     * 获取jssdk
     * @param url 参数
     * @return ResponseResult
     */
    public ResponseResult getJsSDk(String url){
        List<String> jsApiList = new ArrayList<>();
        //需要用到哪些JS SDK API 就设置哪些
        jsApiList.add("onMenuShareTimeline");
        jsApiList.add("onMenuShareAppMessage");
        jsApiList.add("onMenuShareQQ");
        jsApiList.add("onMenuShareWeibo");
        jsApiList.add("onMenuShareQZone");
        jsApiList.add("closeWindow");
        jsApiList.add("scanQRCode");
        try {
            //把config返回到前端进行js调用即可。
            WxJsapiConfig config = wxService.createJsapiConfig(url, jsApiList);
            return ResponseUtil.setSuccessResult();
        } catch (WxErrorException e) {
            logger.error(e.toString()+"获取jssdk失败");
            return ResponseUtil.setSuccessResult();
        }
    }

    /**
     * 获取opendid
     * @param code code
     * @return openid
     * @throws WxErrorException 异常
     */
    public String getOPenidByCode(String code) throws WxErrorException {
        WxOAuth2AccessTokenResult wxOAuth2AccessTokenResult = wxService.oauth2ToGetAccessToken(code);
        return wxOAuth2AccessTokenResult.getOpenid();
    }


    /**
     * 获取opendid
     * @param code code
     * @return openid
     * @throws WxErrorException 异常
     */
    public String getOpenid(String code) throws Exception{
        String url =  String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                environment.getProperty("weixin.appid"),environment.getProperty("weixin.secret"),code);
        String info = new RestTemplate().getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(info);

        return json.getString("openid");
    }



}
