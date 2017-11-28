package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.service.LoginManager;
import com.acmed.his.service.WxManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.soecode.wxtools.exception.WxErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @GetMapping
    @ResponseBody
    public ResponseResult<RequestToken> userlogin(
            @ApiParam("登录名、或者手机号") @RequestParam(value = "loginName",defaultValue = "187394859342") String loginName,
            @ApiParam("密码") @RequestParam(value = "passwd",defaultValue = "==wMwIjM1ETM1ITMxUTMhZTN0MjMxs0a1F2V") String passwd) throws Exception{
        ResponseResult<RequestToken> result = loginManager.userlogin(loginName,passwd);
        return result;
    }

    @ApiOperation("根据code获取token")
    @GetMapping(value = "/code")
    public ResponseResult<RequestToken> openid(
            @ApiParam(value = "code") @RequestParam("code") String code){
        String openid = null;
        try {
            openid = wxManager.getOPenidByCode(code);
        } catch (WxErrorException e) {
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

}
