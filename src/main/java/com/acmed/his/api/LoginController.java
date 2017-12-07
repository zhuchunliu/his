package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.pojo.mo.UserLoginMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.service.LoginManager;
import com.acmed.his.service.WxManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.soecode.wxtools.exception.WxErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.WeakHashMap;

/**
 * Created by Darren on 2017-11-21
 **/
@RestController
@Api(tags ="登录接口")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private WxManager wxManager;

    @ApiOperation("用户登录")
    @PostMapping
    @WithoutToken
    public ResponseResult<RequestToken> userlogin(@RequestBody UserLoginMo mo) throws Exception{
        Map<String, Object> paramMap = new WeakHashMap<>();
        paramMap.put("loginName",mo.getLoginName());
        paramMap.put("passwd",mo.getPasswd());
        ResponseResult result = ResponseUtil.getParamEmptyError(paramMap);
        if(null != result){
            return result;
        }
        result = loginManager.userlogin(mo.getLoginName(),mo.getPasswd());
        if(!result.isSuccess()){
            return result;
        }
        String token = Optional.ofNullable(result)
                .map(obj->(RequestToken)obj.getResult()).map(obj->obj.getToken()).orElse(null);
        Map<String,String > map = new HashMap<>();
        map.put("token",token);
        return ResponseUtil.setSuccessResult(map);
    }

    @ApiOperation("根据code获取token")
    @GetMapping(value = "/code")
    @WithoutToken
    public ResponseResult<RequestToken> openid(
            @ApiParam(value = "code") @RequestParam("code") String code){
        String openid = null;
        try {
            openid = wxManager.getOpenid(code);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_GETOPENIDECORD,"获取openid异常");
        }
        RequestToken requestToken =  loginManager.getTokenByOpenid(openid);
        return ResponseUtil.setSuccessResult(requestToken);
    }



    @ApiOperation("刷新token")
    @GetMapping(value = "/tokenRefresh")
    public ResponseResult<UserInfo> tokenRefresh(
            @ApiParam(value = "header中必须传token") @RequestHeader(value = CommonConstants.USER_HEADER_TOKEN) String token){
        loginManager.tokenRefresh(token);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("退出登录")
    @GetMapping(value = "/logout")
    public ResponseResult<UserInfo> logout(
            @ApiParam(value = "header中必须传token") @RequestHeader(value = CommonConstants.USER_HEADER_TOKEN) String token){
        loginManager.logout(token);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("根据token获取用户信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping(value = "/user")
    public ResponseResult<UserInfo> getUser(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(info.getUser());
    }

}
