package com.acmed.his.util;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * NumberFormtUtil
 *
 * @author jimson
 * @date 2017/12/25
 */
public class NumberFormtUtil {
    /**
     * BigDecimal 格式化成两位小数字符串
     * @param bigDecimal
     * @return
     */
    public static String toString2decimal(BigDecimal bigDecimal){
        String s = null;
        DecimalFormat decimalFormat = new DecimalFormat("#.00");
        if (bigDecimal.compareTo(new BigDecimal(0)) == 0){
            s="0.00";
        } else if (bigDecimal.compareTo(new BigDecimal(1)) == -1&& bigDecimal.compareTo(new BigDecimal(0)) ==1){
            s = "0"+decimalFormat.format(bigDecimal).toString();
        }else {

            s = decimalFormat.format(bigDecimal).toString();
        }
        return s;
    }


    /**
     * 保留4位小数
     * @param param 小数
     * @return
     */
    public static float to2xiaoshu(float param){
        return (float)(Math.round(param*100))/100;
    }
}
