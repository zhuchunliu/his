package com.acmed.his.service;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.println;

/**
 * Created by Darren on 2018-05-02
 **/
@BTrace
public class DrugTraceTest {


<<<<<<< Updated upstream
//    @OnMethod(clazz = "com.acmed.his.api.DrugApi",method = "getDrugList",location =
//    @Location(value = Kind.CALL, clazz = "/com.acmed.his.service.*/", method = "/.*/",where = Where.AFTER))
//    public static void getDrugList(@ProbeClassName String className, @ProbeMethodName String methodName,
//                                   @TargetInstance Object instance, @TargetMethodOrField  String method,
//                                   @Duration long duration,
//                                   @Return AnyType value
//    ){
//        println(className+" : "+methodName);
//        println(instance+" "+method+"   "+duration+"****");
=======
    @OnMethod(clazz = "com.acmed.his.api.DrugApi",method = "getDrugList",
            location = @Location(value = Kind.CALL,clazz = "/.*/",method = "/.*/",where = Where.AFTER))
    public static void getDrugList(@ProbeClassName String className, @ProbeMethodName String methodName,
                                   @TargetInstance Object instance,@TargetMethodOrField String method,
                                   @Duration long duration){
        println(className+" : "+methodName+" ***");
        println("instance: "+instance+" method: "+method+" duration: "+duration);
>>>>>>> Stashed changes
//        println(value+"====");
//    }

    @OnMethod(clazz = "com.acmed.his.api.DrugApi",method = "getDrugList",location =
    @Location(value = Kind.CALL, clazz = "/com.acmed.his.service.*/", method = "/.*/",where = Where.AFTER))
    public static void getDrugList(@ProbeClassName String className, @ProbeMethodName String methodName,
                                   @TargetInstance Object instance, @TargetMethodOrField  String method,
                                   @Duration long duration,
                                   @Return AnyType value){
        println(className+" : "+methodName);
        println(instance+" "+method+"   "+duration+"****");
        println(value+"====");
    }


//    @OnMethod(clazz = "com.acmed.his.util.DateTimeUtil",method = "getAge")
//    public static void getDrugListCopy(@ProbeClassName String className,@ProbeMethodName String methodName){
//        println(className+" : "+methodName);
//        println("get list");
//    }

//    @OnMethod(clazz = "com.acmed.his.api.ScheduleApi",method = "list",location =
//    @Location(value = Kind.CALL, clazz = "/com.acmed.his.util.*/", method = "/.*/",where = Where.AFTER))
//    public static void getApply(@ProbeClassName String className,@ProbeMethodName String methodName,@TargetInstance Object instance, @TargetMethodOrField  String method,
//                                   @Duration long duration){
//        println(className+" : "+methodName);
//        println(instance+" "+method+"   "+duration+"****");
//    }

//    @OnMethod(clazz = "com.acmed.his.pojo.vo.DrugListVo",method = "<init>")
//    public static void initDrugListVo(){
//        println("======init object===========");
//    }


}
