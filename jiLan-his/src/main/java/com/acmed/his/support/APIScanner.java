package com.acmed.his.support;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**
 * Created by Darren on 2017-12-06
 **/
public class APIScanner {

    public static List<String> getAPIByAnnotation(String packName,Class annotation){
        Preconditions.checkArgument(annotation.isAnnotation(), "参数必须是@interface!");
        List<String> result = Lists.newLinkedList();
        List<Class<?>> controllerClasses = Lists.newArrayList();
        Reflections reflections = new Reflections(packName);
        controllerClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        controllerClasses.addAll(reflections.getTypesAnnotatedWith(RestController.class));

        for(Class clazz : controllerClasses){
            RequestMapping classMappingInfo = (RequestMapping)clazz.getAnnotation(RequestMapping.class);
            String rootUrl = classMappingInfo == null ? "" : removeEndSlash(classMappingInfo.value().length > 0 ? classMappingInfo.value()[0] : "");
            for (Method method : clazz.getDeclaredMethods()) {
                if(Modifier.isPublic(method.getModifiers()) && (method.isAnnotationPresent(annotation) || clazz.isAnnotationPresent(annotation))){
                    RequestMapping mappingInfo = method.getAnnotation(RequestMapping.class);
                    if(null != mappingInfo){
                        Preconditions.checkArgument(mappingInfo.method().length < 2, "一个java方法只支持一种http请求方法");
                        for (int i = 0; i < mappingInfo.value().length; i++) {
                            String httpMethod = mappingInfo.method().length == 1 ? mappingInfo.method()[0].toString() : "GET";
                            String urlInfo = httpMethod + rootUrl + removeEndSlash(mappingInfo.value()[i]);
                            result.add(urlInfo);
                        }

                        for (int i = 0; i < mappingInfo.path().length; i++) {
                            String httpMethod = mappingInfo.method().length == 1 ? mappingInfo.method()[0].toString() : "GET";
                            String urlInfo = httpMethod + rootUrl + removeEndSlash(mappingInfo.path()[i]);
                            result.add(urlInfo);
                        }

                        continue;
                    }

                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    if (getMapping != null) {
                        String path = "";
                        if (getMapping.value().length == 0 && getMapping.path().length == 0) {

                        } else if (getMapping.value().length > getMapping.path().length) {
                            path = getMapping.value()[0];
                        } else {
                            path = getMapping.path()[0];
                        }
                        String urlInfo = "GET" + rootUrl + removeEndSlash(path);
                        result.add(urlInfo);
                        continue;
                    }

                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    if (postMapping != null) {
                        String path = "";
                        if (postMapping.value().length == 0 && postMapping.path().length == 0) {

                        } else if (postMapping.value().length > postMapping.path().length) {
                            path = postMapping.value()[0];
                        } else {
                            path = postMapping.path()[0];
                        }
                        String urlInfo = "POST" + rootUrl + removeEndSlash(path);
                        result.add(urlInfo);
                        continue;
                    }

                    PutMapping putMapping = method.getAnnotation(PutMapping.class);
                    if (putMapping != null) {
                        String path = "";
                        if (putMapping.value().length == 0 && putMapping.path().length == 0) {

                        } else if (putMapping.value().length > putMapping.path().length) {
                            path = putMapping.value()[0];
                        } else {
                            path = putMapping.path()[0];
                        }
                        String urlInfo = "PUT" + rootUrl + removeEndSlash(path);
                        result.add(urlInfo);
                        continue;
                    }

                    DeleteMapping deleteMapping = method.getAnnotation(DeleteMapping.class);
                    if (deleteMapping != null) {
                        String path = "";
                        if (deleteMapping.value().length == 0 && deleteMapping.path().length == 0) {

                        } else if (deleteMapping.value().length > deleteMapping.path().length) {
                            path = deleteMapping.value()[0];
                        } else {
                            path = deleteMapping.path()[0];
                        }
                        String urlInfo = "DELETE" + rootUrl + removeEndSlash(path);
                        result.add(urlInfo);
                        continue;
                    }

                }
            }
        }
        return result;
    }

    private static String removeEndSlash(String str) {
        if ("/".equals(str) || "".equals(str)) {//special case
            return "";
        }
        str = StringUtils.removeEnd(str, "/");
        return str.startsWith("/") ? str : ("/" + str);
    }
}
