package com.acmed.his.exceptions.handler;

import com.acmed.his.exceptions.BaseException;
import com.acmed.his.exceptions.ErrorMsg;
import com.acmed.his.exceptions.view.JsonModelAndViewBuilder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Darren on 2017-11-24
 **/
public class ServiceExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        if(ex instanceof BaseException){
            response.setStatus(200);
            return JsonModelAndViewBuilder.build(new ErrorMsg((BaseException)ex));
        }
        return null;
    }
}
