package com.acmed.his.support;


import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.PatientMapper;
import com.acmed.his.dao.UserMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Patient;
import com.acmed.his.model.User;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.service.PatientManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.TokenUtil;
import com.google.common.io.BaseEncoding;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-23
 **/
public class AccessTokenResolver implements HandlerMethodArgumentResolver  {

    private PatientMapper patientMapper;
    private UserMapper userMapper;
    private PatientManager patientManager;


    public AccessTokenResolver(ApplicationContext applicationContext) {
        this.patientMapper = applicationContext.getBean(PatientMapper.class);
        this.userMapper = applicationContext.getBean(UserMapper.class);
        this.patientManager = applicationContext.getBean(PatientManager.class);
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AccessToken.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        AccessToken annotation = parameter.getParameterAnnotation(AccessToken.class);

        if(!annotation.required()){
            return new AccessInfo();
        }

        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        String token = servletRequest.getHeader(CommonConstants.USER_HEADER_TOKEN);
        if(StringUtils.isEmpty(token)){
            throw new BaseException(StatusCode.ERROR_TOKEN);
        }

        String loginId = Optional.ofNullable(token)
                .map(TokenUtil::getFromToken).map(ResponseResult::getResult)
                .map(val ->(RequestToken)val).map(RequestToken::getLoginid).orElse(null);
        User user = null;
        Patient patient = null;
        Integer source = null;
        if(loginId.startsWith("PATIENT_WX")){
            source = 1;
            patient = patientMapper.selectByPrimaryKey(Integer.parseInt(loginId.substring("PATIENT_WX".length())));
            user = Optional.ofNullable(patient.getOpenid()).map((obj)->userMapper.getUserByOpenid(obj)).orElse(null);
        }
        else if(loginId.startsWith("USER_WX")){
            source = 2;
            user = userMapper.selectByPrimaryKey(Integer.parseInt(loginId.substring("USER_WX".length())));
            patient = Optional.ofNullable(user.getOpenid()).map((obj)->patientManager.getPatientByOpenid(obj)).orElse(null);
        }
        else if(loginId.startsWith("USER_PAD")){
            source = 3;
            user = userMapper.selectByPrimaryKey(Integer.parseInt(loginId.substring("USER_PAD".length())));
        }

        return new AccessInfo(patient,user,source);
    }



}
