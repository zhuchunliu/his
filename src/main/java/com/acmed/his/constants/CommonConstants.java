package com.acmed.his.constants;

/**
 * 公共常量
 * Created by Issac on 2017/11/13 0013.
 */
public class CommonConstants {

    /**true*/
    public static final Integer TRUE = 0;

    /**false*/
    public static final Integer FALSE = 1;

    /**FMC->ECG*/
    public static final String REPORT_TIME_CATEGORY_FMC_ECG = "0";

    /**ECG->STEM*/
    public static final String REPORT_TIME_CATEGORY_ECG_STEMI = "1";

    /**D->B*/
    public static final String REPORT_TIME_CATEGORY_DTOB = "2";

    /**CR激活时间*/
    public static final String REPORT_TIME_CATEGORY_ACTIVE_CR = "3";

    /**空闲中*/
    public static final String CAROUT_FREE_STATUS = "20";

    /**验证码超时秒数*/
    public static final Integer VALID_CODE_EXPIRE_SECONDS = 60;

    /**登录信息超时秒数*/
    public static final Integer LOGININFO_EXPIRE_SECONDS = 1800;

    /**发送短信回执成功码*/
    public static final String SMS_ERRORMSG_OK = "OK";

    /**用户Header token*/
    public static final String USER_HEADER_TOKEN = "Authorization";
}
