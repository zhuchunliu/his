package com.acmed.his.role;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import io.swagger.annotations.Api;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

/**
 * Created by Darren on 2018-01-25
 **/
public class RoleTest {
    public static void main(String[] args) {
        List<Class<?>> controllerClasses = Lists.newArrayList();
        Reflections reflections = new Reflections("com.acmed.his.api");
        controllerClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        controllerClasses.addAll(reflections.getTypesAnnotatedWith(RestController.class));
        for(Class<?> clazz :controllerClasses){
            Api api = clazz.getAnnotation(io.swagger.annotations.Api.class);
            System.err.println(clazz.getName()+" == "+api.tags()[0]);
        }
    }
}
