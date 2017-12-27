package com.acmed.his.constants;

/**
 * Created by Darren on 2017-11-21
 */
public class AppConstants {
    public static final String  PASSWORD_BUILD ="%s%s%s";//随机数5位+密码+timestamp
    public static final String  TOKEN_BUILD ="%s%s";//APPKEY或用户id+（20位随机数+timestamp）
    public static final String  APP_SIGN_BUILD="%s%s";//APPKEY+随机数5位
}
