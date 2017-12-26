package com.acmed.his.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * WaterCodeUtil
 *
 * @author jimson
 * @date 2017/12/22
 */
public class WaterCodeUtil {
    /**
     * 生成流水号
     * @return 流水号
     */
    public static String getWaterCode(){
        String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String seconds = new SimpleDateFormat("HHmmss").format(new Date());
        return date+"00001000"+getTwo()+"00"+seconds+getTwo();
    }

    /**
     * 获取六位短信验证码
     * @return 获取六位短信验证码
     */
    public static String getSixSmsCode(){
        int i = (int) ((Math.random() * 9 + 1) * 100000);
        return i+"";
    }

    /**
     * 产生随机的2位数
     * @return
     */
    public static String getTwo(){
        Random rad=new Random();

        String result  = rad.nextInt(100) +"";

        if(result.length()==1){
            result = "0" + result;
        }
        return result;
    }
}
