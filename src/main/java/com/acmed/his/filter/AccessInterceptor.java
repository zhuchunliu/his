package com.acmed.his.filter;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.service.PermissionManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.Optional;

/**
 * 权限拦截
 *
 * Created by Darren on 2017-11-27
 **/
public class AccessInterceptor implements HandlerInterceptor {

    private PermissionManager permissionManager;

    public AccessInterceptor(ApplicationContext applicationContext) {
        this.permissionManager = applicationContext.getBean(PermissionManager.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {

        String path = request.getServletPath();
        if(path.equals("/login")){//登录接口不拦截
            return true;
        }
        String token = request.getHeader(CommonConstants.USER_HEADER_TOKEN);
        if(StringUtils.isEmpty(token)){
            throw new BaseException(StatusCode.ERROR_TOKEN);
        }

        String loginId = Optional.ofNullable(token)
                .map(TokenUtil::getFromToken).map(ResponseResult::getResult)
                .map(val ->(RequestToken)val).map(RequestToken::getLoginid).orElse(null);
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
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
