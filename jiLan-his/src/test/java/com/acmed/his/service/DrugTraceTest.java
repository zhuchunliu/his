package com.acmed.his.service;

import com.sun.btrace.AnyType;
import com.sun.btrace.annotations.*;

import static com.sun.btrace.BTraceUtils.println;

/**
 * Created by Darren on 2018-05-02
 **/
@BTrace
public class DrugTraceTest {


    @OnMethod(clazz = "com.acmed.his.api.DrugApi",method = "/.*/")
    public static void getDrugList(@ProbeClassName String className, @ProbeMethodName String methodName){
//                                   @Return AnyType value){
        println("****************");
        println(className+" : "+methodName);
//        println(value+"====");
    }


//    @OnMethod(clazz = "com.acmed.his.api.DrugApi",location = @Location(value = Kind.LINE,line = 82))
//    public static void getDrugListCopy(@ProbeClassName String className,@ProbeMethodName String methodName){
//        println(className+" : "+methodName);
//        println("get list");
//    }
//
//    @OnMethod(clazz = "com.acmed.his.pojo.vo.DrugListVo",method = "<init>")
//    public static void initDrugListVo(){
//        println("======init object===========");
//    }


}
