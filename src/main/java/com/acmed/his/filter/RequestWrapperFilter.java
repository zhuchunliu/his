package com.acmed.his.filter;

import com.acmed.his.filter.servlet.CustomServlteRequest;

import javax.servlet.*;
import java.io.IOException;

/**
 * Created by Darren on 2017-11-28
 **/
public class RequestWrapperFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new CustomServlteRequest(servletRequest),servletResponse);
    }

    @Override
    public void destroy() {

    }
}
