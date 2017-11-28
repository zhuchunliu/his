package com.acmed.his.filter.interceptor;

import com.acmed.his.util.IPAddressUtil;
import com.google.common.base.Charsets;
import okio.Okio;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by Darren on 2017-11-28
 **/
public class GateInterceptor implements HandlerInterceptor {

    private Logger logger = LoggerFactory.getLogger(GateInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String path = request.getServletPath();
        if(path.contains("swagger") || path.contains("api-docs")){
            return true;
        }
        logger.info("ip: "+IPAddressUtil.parse(request));
        logger.info("url: "+request.getMethod() + path);
        logger.info("query: "+ request.getQueryString());
        String body = Okio.buffer(Okio.source(request.getInputStream())).readString(Charsets.UTF_8);
        if (StringUtils.isNotBlank(body)) {
            logger.info("body: "+ body);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
