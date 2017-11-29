package com.acmed.his.filter.interceptor;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.RedisKeyConstants;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.service.LoginManager;
import com.acmed.his.service.PermissionManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Optional;

/**
 * 权限拦截
 *
 * Created by Darren on 2017-11-27
 **/
public class AccessInterceptor implements HandlerInterceptor {

    private PermissionManager permissionManager;
    private LoginManager loginManager;
    private RedisTemplate redisTemplate;


    public AccessInterceptor(ApplicationContext applicationContext) {
        this.permissionManager = applicationContext.getBean(PermissionManager.class);
        this.loginManager = applicationContext.getBean(LoginManager.class);
        this.redisTemplate = applicationContext.getBean("redisTemplate",RedisTemplate.class);
        this.redisTemplate.setKeySerializer(new StringRedisSerializer());
        this.redisTemplate.setValueSerializer(new StringRedisSerializer());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String path = request.getServletPath();
        if(path.contains("swagger") || path.contains("api-docs") || path.equals("/login")){
            return true;
        }
        String token = request.getHeader(CommonConstants.USER_HEADER_TOKEN);
        if(StringUtils.isEmpty(token)){
            throw new BaseException(StatusCode.ERROR_TOKEN);
        }

        String loginId = Optional.ofNullable(token)
                .map(TokenUtil::getFromToken).map(ResponseResult::getResult)
                .map(val ->(RequestToken)val).map(RequestToken::getLoginid).orElse(null);

        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        Map<Object, Object> map = hash.entries(String.format(RedisKeyConstants.USERKEY_PRE, loginId));
        if(null == map || !map.containsKey(RedisKeyConstants.USERTOKEN_PRE) || !map.get(RedisKeyConstants.USERTOKEN_PRE).equals(token)){//token不存在或过期
            throw new BaseException(StatusCode.ERROR_TOKEN);
        }
        if(loginId.startsWith("PATIENT_WX")){//患者权限暂不控制
            return true;
        } else{
            String uid = loginId.startsWith("USER_PAD")?loginId.substring("USER_PAD".length()):loginId.substring("USER_WX".length());
            boolean flag =  permissionManager.hasPermission(uid,path);
            if(!flag){
                response.setContentType("application/json;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("{\"success\":false,\"statusCode\":\""+StatusCode.ERROR_PREMISSION+"\",\"msg\":\""+StatusCode.ERROR_PREMISSION.getErrorMsg()+"\"}");
                writer.close();
            }
            return flag;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {
        String token = request.getHeader(CommonConstants.USER_HEADER_TOKEN);
        if(!StringUtils.isEmpty(token) && !request.getServletPath().contains("logout")){
            loginManager.tokenRefresh(token);
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
