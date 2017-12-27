package com.acmed.his.util;

/**
 * 常量
 */
public class WXPayConstants {

    public enum SignType {
        MD5, HMACSHA256
    }

//    public static final String DOMAIN_API = "https://api.mch.weixin.qq.com";
//    public static final String DOMAIN_API2 = "api2.mch.weixin.qq.com";
//    public static final String DOMAIN_APIHK = "apihk.mch.weixin.qq.com";
//    public static final String DOMAIN_APIUS = "apius.mch.weixin.qq.com";


    public static final String FAIL     = "FAIL";
    public static final String SUCCESS  = "SUCCESS";
    public static final String HMACSHA256 = "HMAC-SHA256";
    public static final String MD5 = "MD5";

    public static final String FIELD_SIGN = "sign";
    public static final String FIELD_SIGN_TYPE = "sign_type";

    public static final String MICROPAY_URL_SUFFIX     = "https://api.mch.weixin.qq.com/pay/micropay";
    public static final String UNIFIEDORDER_URL_SUFFIX = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String ORDERQUERY_URL_SUFFIX   = "https://api.mch.weixin.qq.com/pay/orderquery";
    public static final String REVERSE_URL_SUFFIX      = "https://api.mch.weixin.qq.com/secapi/pay/reverse";
    public static final String CLOSEORDER_URL_SUFFIX   = "https://api.mch.weixin.qq.com/pay/closeorder";
    public static final String REFUND_URL_SUFFIX       = "https://api.mch.weixin.qq.com/secapi/pay/refund";
    public static final String REFUNDQUERY_URL_SUFFIX  = "https://api.mch.weixin.qq.com/pay/refundquery";
    public static final String DOWNLOADBILL_URL_SUFFIX = "https://api.mch.weixin.qq.com/pay/downloadbill";
    public static final String REPORT_URL_SUFFIX       = "https://api.mch.weixin.qq.com/payitil/report";
    public static final String SHORTURL_URL_SUFFIX     = "https://api.mch.weixin.qq.com/tools/shorturl";
    public static final String AUTHCODETOOPENID_URL_SUFFIX = "https://api.mch.weixin.qq.com/tools/authcodetoopenid";

    // sandbox
    public static final String SANDBOX_MICROPAY_URL_SUFFIX     = "https://api.mch.weixin.qq.com/sandboxnew/pay/micropay";
    public static final String SANDBOX_UNIFIEDORDER_URL_SUFFIX = "https://api.mch.weixin.qq.com/sandboxnew/pay/unifiedorder";
    public static final String SANDBOX_ORDERQUERY_URL_SUFFIX   = "https://api.mch.weixin.qq.com/sandboxnew/pay/orderquery";
    public static final String SANDBOX_REVERSE_URL_SUFFIX      = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/reverse";
    public static final String SANDBOX_CLOSEORDER_URL_SUFFIX   = "https://api.mch.weixin.qq.com/sandboxnew/pay/closeorder";
    public static final String SANDBOX_REFUND_URL_SUFFIX       = "https://api.mch.weixin.qq.com/sandboxnew/secapi/pay/refund";
    public static final String SANDBOX_REFUNDQUERY_URL_SUFFIX  = "https://api.mch.weixin.qq.com/sandboxnew/pay/refundquery";
    public static final String SANDBOX_DOWNLOADBILL_URL_SUFFIX = "https://api.mch.weixin.qq.com/sandboxnew/pay/downloadbill";
    public static final String SANDBOX_REPORT_URL_SUFFIX       = "https://api.mch.weixin.qq.com/sandboxnew/payitil/report";
    public static final String SANDBOX_SHORTURL_URL_SUFFIX     = "https://api.mch.weixin.qq.com/sandboxnew/tools/shorturl";
    public static final String SANDBOX_AUTHCODETOOPENID_URL_SUFFIX = "https://api.mch.weixin.qq.com/sandboxnew/tools/authcodetoopenid";


}

