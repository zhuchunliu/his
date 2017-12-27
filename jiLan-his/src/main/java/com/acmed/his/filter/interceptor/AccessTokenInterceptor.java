package com.acmed.his.filter.interceptor;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.RedisKeyConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.support.APIScanner;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.TokenUtil;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 权限拦截
 *
 * Created by Darren on 2017-11-27
 **/
public class AccessTokenInterceptor implements HandlerInterceptor {

    private RedisTemplate redisTemplate;

    public AccessTokenInterceptor(ApplicationContext applicationContext) {
        this.redisTemplate = applicationContext.getBean("stringRedisTemplate",RedisTemplate.class);
    }

    private static final ImmutableSet<String> ignore;

    static {
        List<String> parseFromPackage = APIScanner.getAPIByAnnotation("com.acmed.his.api", WithoutToken.class);
        ignore = ImmutableSet.copyOf(parseFromPackage);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String path = request.getServletPath();
        if(path.contains("swagger") || path.contains("api-docs")){
            return true;
        }

        if(ignore.contains(request.getMethod()+request.getServletPath())){
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
        request.setAttribute("loginId",loginId);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
