package com.acmed.his.exceptions.handler;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.exceptions.ErrorMsg;
import com.acmed.his.exceptions.view.JsonModelAndViewBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by Darren on 2017-11-24
 **/
public class ServiceExceptionHandler implements HandlerExceptionResolver {

    private Logger logger = LoggerFactory.getLogger(ServiceExceptionHandler.class);
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object o, Exception ex) {
        if(ex instanceof BaseException){
            response.setStatus(200);
            BaseException baseException = (BaseException)ex;
            return JsonModelAndViewBuilder.build(
                    new ErrorMsg(baseException.getStatusCode(),baseException.getMsg()));
        }else{
            logger.error(ex.getMessage(),ex);
            response.setStatus(200);
            return JsonModelAndViewBuilder.build(new ErrorMsg(StatusCode.FAIL,"程序出错，请联系管理员！"));
        }
    }
}
