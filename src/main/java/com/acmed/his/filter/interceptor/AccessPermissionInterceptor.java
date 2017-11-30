package com.acmed.his.filter.interceptor;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.service.PermissionManager;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Created by Darren on 2017-11-30
 **/
public class AccessPermissionInterceptor implements HandlerInterceptor {

    private PermissionManager permissionManager;

    public AccessPermissionInterceptor(ApplicationContext applicationContext) {
        this.permissionManager = applicationContext.getBean(PermissionManager.class);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String path = request.getServletPath();
        if(path.contains("swagger") || path.contains("api-docs") || path.equals("/login")){
            return true;
        }
        String loginId = request.getAttribute("loginId").toString();
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
