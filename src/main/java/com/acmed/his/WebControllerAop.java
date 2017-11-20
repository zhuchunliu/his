
package com.acmed.his;


import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

/**
 * 请求前置处理
 * created by Issac 2017/08/25.
 */
@Component
@Aspect
public class WebControllerAop {
    private final static Logger logger =  LoggerFactory.getLogger(WebControllerAop.class);

//    @Value("${spring.profiles}")
//    private String env;

    //匹配controller包及其子包下的所有类的所有方法
    @Pointcut("execution(* com.acmed.his.api..*.*(..))")
    public void webLog(){

    }

    /**
     * 前置通知，方法调用前被调用
     * @param joinPoint
     */
    @Before("webLog()")
    public void doBeforeAdvice(JoinPoint joinPoint){
        logger.info("WebLogAspect.doBeforeAdvice()");
        //获取目标方法的参数信息
        Object[] obj = joinPoint.getArgs();
        //AOP代理类的信息
        joinPoint.getThis();
        //代理的目标对象
        joinPoint.getTarget();
        //用的最多 通知的签名
        Signature signature = joinPoint.getSignature();
        signature.getDeclaringType();
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();

        // 记录下请求内容
        logger.info("URL : " + request.getRequestURL().toString());
        logger.info("HTTP_METHOD : " + request.getMethod());
        logger.info("IP : " + request.getRemoteAddr());
        logger.info("CLASS_METHOD : " + joinPoint.getSignature().getDeclaringTypeName() + "."
                 + joinPoint.getSignature().getName());
        logger.info("ARGS : " + Arrays.toString(joinPoint.getArgs()));

        Enumeration<String> enumeration = request.getParameterNames();
        Map<String,String> parameterMap = Maps.newHashMap();
        while (enumeration.hasMoreElements()){
            String parameter = enumeration.nextElement();
            parameterMap.put(parameter,request.getParameter(parameter));
        }
        String str = JSON.toJSONString(parameterMap);
        if(obj.length > 0) {
            logger.info("请求的参数信息为："+str);
        }


    }

//
//    @Around("webLog()")
//    public Object authInteceptor(ProceedingJoinPoint pjp) throws Throwable {
//
//        ServletRequestAttributes attributes =
//                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        //通过header头域判断是否登录
//        String token = request.getHeader("Authorization");
//        logger.info("用户认证开始，当前token是" + token);
//        boolean isAuth = true; // StringUtils.isNotBlank(token) && CheckUserLoginUtils.isLoginUser(token);
//        return isAuth ? pjp.proceed() : ResponseUtil.setErrorMeg(StatusCode.ERROR_AUTH, "认证失败");
//    }


   /* @AfterReturning("webLog()")
    public void  doAfterReturning(JoinPoint joinPoint){
        // 处理完请求，返回内容
        logger.info("WebLogAspect.doAfterReturning()");
    }*/

    @AfterReturning(returning = "ret", pointcut = "webLog()")
    public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);
    }

}
