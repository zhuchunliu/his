package com.acmed.his.role;

import com.acmed.his.api.PrescriptionApi;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import org.reflections.Reflections;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-01-25
 **/
public class RoleTest {

    public static Map<String,List<Class>> map = Maps.newHashMap();
    public static Map<String,String> classNameMap = Maps.newHashMap();
    static {
        classNameMap.put("PrescriptionApi","看病就诊");
        classNameMap.put("ApplyApi","就诊列表");
        classNameMap.put("ScheduleApi","医生排班");
        classNameMap.put("PatientItemApi","患者库");

        classNameMap.put("","");

        map.put("看病就诊",Lists.newArrayList(PrescriptionApi.class));
    }

    public static void main(String[] args) {
        List<Class<?>> controllerClasses = Lists.newArrayList();
        Reflections reflections = new Reflections("com.acmed.his.api");
        controllerClasses.addAll(reflections.getTypesAnnotatedWith(Controller.class));
        controllerClasses.addAll(reflections.getTypesAnnotatedWith(RestController.class));
        for(Class<?> clazz :controllerClasses){
            Api api = clazz.getAnnotation(io.swagger.annotations.Api.class);
            if(api.hidden()){
                continue;
            }
            System.err.println(clazz.getName()+" == "+api.tags()[0]);
        }
    }
}
