package com.acmed.his.api;

import com.acmed.his.pojo.vo.WxConfig;
import com.acmed.his.service.WxManager;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;



/**
 * WeChatApi
 *
 * @author jimson
 * @date 2018/3/13
 */
@Api(tags = "微信相关")
@RestController
@RequestMapping("/weChat")
public class WeChatApi {

    @Autowired
    private WxManager wxManager;
    /**
     * 获取jssdk
     * @param url
     * @return
     */
    @WithoutToken
    @GetMapping(value = "jssdk")
    public ResponseResult<WxConfig> getJsSdK(@RequestParam(value = "url",required = true)String url){
        return ResponseUtil.setSuccessResult(wxManager.getJssdk(url));
    }
}
