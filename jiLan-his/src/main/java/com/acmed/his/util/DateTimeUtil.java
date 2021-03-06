package com.acmed.his.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * 日期帮助类
 *
 * Created by Darren on 2017-12-19
 **/
public class DateTimeUtil {
    /**
     * 根据生日计算年龄
     * @param birth
     * @return
     */
    public static Integer getAge(String birth){
        if(StringUtils.isEmpty(birth)){
            return null;
        }
        birth = birth.replace("-","");
        if(StringUtils.isEmpty(birth)) return null;
        if(birth.length() == 18){
            birth = birth.substring(6,14);
        }
        if(birth.contains(":")){
            birth = 10 == birth.length()?birth:birth.substring(0,8);
        }


        LocalDate now = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(birth,DateTimeFormatter.ofPattern("yyyyMMdd"));
        int age = now.getYear()-birthDate.getYear();
        if(birthDate.getMonthValue() > now.getMonthValue()) age = age-1;
        if(birthDate.getMonthValue() == now.getMonthValue() && birthDate.getDayOfMonth() > now.getDayOfMonth()) age = age-1;
        return age < 0?0:age;
    }

    public static Date parsetDate(String date){
        if(10 == date.length()) {
            date+=" 00:00:00";
        }
        if(date.length() > 19){
            date = date.substring(0,19);
        }
        return Date.from(LocalDateTime.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")).atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime parsetLocalDateTime(String date){
        if(10 == date.length()) {
            date+=" 00:00:00";
        }
        if(date.length() > 19){
            date = date.substring(0,19);
        }
        return LocalDateTime.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDate parsetLocalDate(String date){
        if(date.length() == 19){
            date = date.substring(0,10);
        }
        return LocalDate.parse(date);
    }


    public static LocalDateTime parsetLocalDateStart(String date){
        if(10 == date.length()) {
            date+=" 00:00:00";
        }
        if(date.length() > 19){
            date = date.substring(0,10);
        }
        return LocalDateTime.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public static LocalDateTime parsetLocalDateEnd(String date){
        if(10 == date.length()) {
            date+=" 23:59:59";
        }
        if(date.length() > 19){
            date = date.substring(0,10);
        }
        return LocalDateTime.parse(date,DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }


    /**
     * 获取开始日期 查询用
     * @param date
     * @return
     */
    public static String getBeginDate(String date){
        if(10 == date.length()) {
            date+=" 00:00:00";
        }
        return date;
    }

    /**
     * 获取截止日期 查询用
     * @param date
     * @return
     */
    public static String getEndDate(String date){
        if(10 == date.length()) {
            date+=" 23:59:59";
        }
        return date;
    }

    public static void main(String[] args) {
        System.err.println(DateTimeUtil.getAge("000461201806194637"));
//        System.err.println(DateTimeUtil.getAge("20170328"));
//        System.err.println(DateTimeUtil.getAge("000000198712195552"));
//        System.err.println(DateTimeUtil.getAge("1987-12-18 10:10:10"));
//        System.err.println(DateTimeUtil.getAge("1987-12-20 10:10:10"));
//        System.err.println(DateTimeUtil.parsetLocalDate("1987-12-20 10:10:10"));
//        System.err.println(DateTimeUtil.parsetDate("1987-12-20"));
    }
}
