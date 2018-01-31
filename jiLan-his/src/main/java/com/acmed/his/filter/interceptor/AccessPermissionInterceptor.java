package com.acmed.his.filter.interceptor;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.Permission;
import com.acmed.his.service.PermissionManager;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by Darren on 2017-11-30
 **/
public class AccessPermissionInterceptor implements HandlerInterceptor {

    private PermissionManager permissionManager;
    private ImmutableSet<String> ignore;

    public AccessPermissionInterceptor(ApplicationContext applicationContext) {
        this.permissionManager = applicationContext.getBean(PermissionManager.class);
        List<Permission> permissionList = permissionManager.getNeedFilterPermissionList();
        List<String> urlList = Lists.newArrayList();
        permissionList.forEach(obj->{
            urlList.add(obj.getUrl());
        });
        ignore = ImmutableSet.copyOf(urlList);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String path = request.getServletPath();
        if(path.contains("swagger") || path.contains("api-docs")){
            return true;
        }

        if(!ignore.contains(request.getServletPath())){
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
