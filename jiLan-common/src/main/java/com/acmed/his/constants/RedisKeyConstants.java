package com.acmed.his.constants;

/**
 * Created by andrew on 2017/6/29.
 */
public class RedisKeyConstants {
    public static final String  USERKEY_PRE ="LOGIN_%s";
    public static final String  USERTOKEN_PRE ="USERTOKEN";
    public static final String  PATIENT_WEIXIN ="PATIENT_WX%s";
    public static final String  USER_WEIXIN ="USER_WX%s";
    public static final String  USER_PAD ="USER_PAD%s";
    public static final String  USER_CODE = "USER_CODE%S";// 修改手机号码时候，短息code存储key
    public static final String  PATIENT_CODE = "PATIENT_CODE%S";// 修改手机号码时候，短息code存储key
    public static final String  WX_BASE_ACCESS_TOKEN ="WX_BASE_ACCESS_TOKEN";

}
