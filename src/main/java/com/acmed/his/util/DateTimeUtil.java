package com.acmed.his.util;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

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
        if(StringUtils.isEmpty(birth)) return null;
        birth = 10 == birth.length()?birth:birth.substring(0,10);

        LocalDate now = LocalDate.now();
        LocalDate birthDate = LocalDate.parse(birth,DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        int age = now.getYear()-birthDate.getYear();
        if(birthDate.getMonthValue() < now.getMonthValue()) return age-1;
        if(birthDate.getDayOfMonth() < now.getDayOfMonth()) return age-1;
        return age;
    }

    public static void main(String[] args) {
        System.err.println(DateTimeUtil.getAge("1987-12-19"));
        System.err.println(DateTimeUtil.getAge("1987-12-18 10:10:10"));
        System.err.println(DateTimeUtil.getAge("1987-12-20 10:10:10"));
    }
}
