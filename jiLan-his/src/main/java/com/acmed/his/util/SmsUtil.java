package com.acmed.his.util;

import com.cloopen.rest.sdk.CCPRestSmsSDK;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * SmsUtil
 *
 * @author jimson
 * @date 2018/1/9
 */
public class SmsUtil {
    private static final String SERVERIP = "app.cloopen.com";
    private static final String SERVERPORT = "8883";
    private static final String ACOUNT_SID = "8aaf07085b3bb22e015b4280cbe405c6";
    private static final String ACOUNT_TOKEN = "895092be3aa54c6cad13d7e5e936c194";
    private static final String APPID = "8aaf07085b3bb22e015b4280cc3005ca";


    /**
     *  发送短信
     * @param mobile 字符串类型，短信接收手机号码集合,用英文逗号分开,如 "13810001000, 最多一次发送200个。
     * @param type 短信类型
     * @param datas 字符串数组类型，内容数据，需定义成数组方式，如模板中有两个参数，定义方式为String{"3456"，"1"}。
     * @return map
     */
    public static Map sendSms(String mobile, Integer type, String[] datas){
        HashMap<String, Object> result = null;
        //初始化SDK
        CCPRestSmsSDK restAPI = new CCPRestSmsSDK();
        //******************************注释*********************************************
        //*初始化服务器地址和端口                                                       *
        //*沙盒环境（用于应用开发调试）：restAPI.init("sandboxapp.cloopen.com", "8883");*
        //*生产环境（用户应用上线使用）：restAPI.init("app.cloopen.com", "8883");       *
        //*******************************************************************************
        restAPI.init(SERVERIP, SERVERPORT);
        //******************************注释*********************************************
        //*初始化主帐号和主帐号令牌,对应官网开发者主账号下的ACCOUNT SID和AUTH TOKEN     *
        //*ACOUNT SID和AUTH TOKEN在登陆官网后，在“应用-管理控制台”中查看开发者主账号获取*
        //*参数顺序：第一个参数是ACOUNT SID，第二个参数是AUTH TOKEN。                   *
        //*******************************************************************************
        restAPI.setAccount(ACOUNT_SID, ACOUNT_TOKEN);
        //******************************注释*********************************************
        //*初始化应用ID                                                                 *
        //*测试开发可使用“测试Demo”的APP ID，正式上线需要使用自己创建的应用的App ID     *
        //*应用ID的获取：登陆官网，在“应用-应用列表”，点击应用名称，看应用详情获取APP ID*
        //*******************************************************************************
        restAPI.setAppId(APPID);
        //******************************注释****************************************************************
        //*调用发送模板短信的接口发送短信                                                                  *
        //*参数顺序说明：                                                                                  *
        //*第一个参数:是要发送的手机号码，可以用逗号分隔，一次最多支持100个手机号                          *
        //*第二个参数:是模板ID，在平台上创建的短信模板的ID值；测试的时候可以使用系统的默认模板，id为1。    *
        //*系统默认模板的内容为“【云通讯】您使用的是云通讯短信模板，您的验证码是{1}，请于{2}分钟内正确输入”*
        //*第三个参数是要替换的内容数组。																														       *
        //**************************************************************************************************
        //**************************************举例说明***********************************************************************
        //*假设您用测试Demo的APP ID，则需使用默认模板ID 1，发送手机号是13800000000，传入参数为6532和5，则调用方式为           *
        //*result = restAPI.sendTemplateSMS("13800000000","1" ,new String[]{"6532","5"});																		  *
        //*则13800000000手机号收到的短信内容是：【云通讯】您使用的是云通讯短信模板，您的验证码是6532，请于5分钟内正确输入     *
        //*********************************************************************************************************************
        Map m = new HashMap();
        String templateId = null;
        switch (type){
            case 1 :
                templateId="167811";
                break;
            default:
                templateId = null;
                break;
        }
        if (StringUtils.isEmpty(templateId)){
            // 模板不正确
            m.put("success",false);
            m.put("code","39991000");
            m.put("msg","短信类型不正确");
            return m;
        }
        result = restAPI.sendTemplateSMS(mobile,templateId ,datas);
        String statusCode = (String) result.get("statusCode");
        if ("000000".equals(statusCode)){
            m.put("success",true);
            return m;
        }
        m.put("success",false);
        m.put("code",statusCode);
        m.put("msg",result.get("statusMsg"));
        return m;
    }
}
