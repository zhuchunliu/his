package com.acmed.his.util;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * IdCardUtil
 *
 * @author jimson
 * @date 2017/11/30
 */
public class IdCardUtil {

    /**
     * 根据身份证号码返回日期  yyyy-mm-dd
     */
    public static LocalDate idCardToDate(String idCard) throws DateTimeException {
        int leh = idCard.length();
        if (leh!=18){
            return null;
        }else {
            Integer year = Integer.valueOf(idCard.substring(6, 10));
            Integer month = Integer.valueOf(idCard.substring(10, 12));
            Integer day = Integer.valueOf(idCard.substring(12, 14));
            return LocalDate.of(year, month, day);
        }
    }
}
