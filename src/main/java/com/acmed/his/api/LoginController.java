package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.service.LoginManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;

/**
 * Created by Darren on 2017-11-21
 **/
@RestController
@Api("登录接口")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private LoginManager loginManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("用户登录")
    @GetMapping
    @ResponseBody
    public ResponseResult<RequestToken> userlogin(
            @ApiParam("登录名、或者手机号") @RequestParam(value = "loginName",defaultValue = "187394859342") String loginName,
            @ApiParam("密码") @RequestParam(value = "passwd",defaultValue = "==wMwIjM1ETM1ITMxUTMhZTN0MjMxs0a1F2V") String passwd) throws Exception{
        ResponseResult<RequestToken> result = loginManager.userlogin(loginName,passwd);
        return result;
    }

    @ApiOperation("根据openid获取token")
    @GetMapping(value = "/openid")
    public ResponseResult<RequestToken> openid(
            @ApiParam(value = "openid") @RequestParam("openid") String openid){
        RequestToken requestToken =  loginManager.getTokenByOpenid(openid);
        return ResponseUtil.setSuccessResult(requestToken);
    }


    @ApiOperation("根据token获取用户信息")
    @GetMapping(value = "/token")
    public ResponseResult<UserInfo> token(
            @ApiParam(value = "header中必须传token") @RequestHeader(value = CommonConstants.USER_HEADER_TOKEN) String token){
        Object object = loginManager.getUserByToken(token);
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(object,userInfo);
        return ResponseUtil.setSuccessResult(userInfo);
    }


    @ApiOperation("刷新token")
    @GetMapping(value = "/tokenRefresh")
    public ResponseResult<UserInfo> tokenRefresh(
            @ApiParam(value = "header中必须传token") @RequestHeader(value = CommonConstants.USER_HEADER_TOKEN) String token){
        redisTemplate.expire(token, CommonConstants.LOGININFO_EXPIRE_SECONDS, TimeUnit.SECONDS);
        return ResponseUtil.setSuccessResult();
    }

}
