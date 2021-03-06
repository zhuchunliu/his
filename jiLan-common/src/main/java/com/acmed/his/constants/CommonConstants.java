package com.acmed.his.constants;

/**
 * 公共常量
 * Created by Issac on 2017/11/13 0013.
 */
public class CommonConstants {

    /**启用状态*/
    public static final String ENABLED_STATUS = "0";

    /**禁用状态*/
    public static final String DISABLED_STATUS = "1";

    /**PAD上TOKEN有效时长*/
    public static final Integer LOGININFO_EXPIRE_SECONDS = 7 * 24 * 60 * 60;

    /**微信TOKEN有效时长*/
    public static final Integer LOGININFO_WEIXIN_EXPIRE_SECONDS = Integer.MAX_VALUE;

    /**发送短信回执成功码*/
    public static final String SMS_ERRORMSG_OK = "OK";

    /**用户Header token*/
    public static final String USER_HEADER_TOKEN = "Authorization";
}
