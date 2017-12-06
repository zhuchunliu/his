package com.acmed.his.constants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * WxPayStatusCode
 *
 * @author jimson
 * @date 2017/12/6
 */
public enum  WxPayStatusCode {
    SUCCESS("SUCCESS", "成功"),// 成功

    NOAUTH("NOAUTH", "商户无此接口权限"),// 商户未开通此接口权限	请商户前往申请此接口权限

    NOTENOUGH("NOTENOUGH", "余额不足"),// 用户帐号余额不足	用户帐号余额不足，请用户充值或更换支付卡后再

    ORDERPAID("ORDERPAID", "订单已支付"),//订单已支付，不能发起关单	订单已支付，不能发起关单，请当作已支付的正常交易

    ORDERCLOSED("ORDERCLOSED", "订单已关闭"),//订单已关闭，无法重复关闭	订单已关闭，无需继续调用

    SYSTEMERROR("SYSTEMERROR", "系统错误"),// 请使用相同参数再次调用API。

    BIZERR_NEED_RETRY("BIZERR_NEED_RETRY", "退款业务流程错误，需要商户触发重试来解决"),// 并发情况下，业务被拒绝，商户重试即可解决

    APPID_NOT_EXIST("APPID_NOT_EXIST", "APPID不存在"),// 检查配置

    MCHID_NOT_EXIST("MCHID_NOT_EXIST", "MCHID不存在"),// 检查配置

    APPID_MCHID_NOT_MATCH("APPID_MCHID_NOT_MATCH", "appid和mch_id不匹配"),// 检查配置

    LACK_PARAMS("LACK_PARAMS", "缺少参数"),// 缺少参数

    OUT_TRADE_NO_USED("OUT_TRADE_NO_USED", "商户订单号重复"),//同一笔交易不能多次提交	请核实商户订单号是否重复提交

    SIGNERROR("SIGNERROR", "签名错误"),//参数签名结果不正确	请检查签名参数和方法是否都符合签名算法要求

    XML_FORMAT_ERROR("XML_FORMAT_ERROR", "XML格式错误"),// XML格式错误	请检查XML参数格式是否正确

    REQUIRE_POST_METHOD("REQUIRE_POST_METHOD", "请使用post方法"),//未使用post传递参数 	请检查请求参数是否通过post方法提交

    POST_DATA_EMPTY("POST_DATA_EMPTY", "post数据为空"),//post数据不能为空	请检查post数据是否为空

    ORDERNOTEXIST("ORDERNOTEXIST", "此交易订单号不存在"),//查询系统中不存在此交易订单号	该API只能查提交支付交易返回成功的订单，请商户检查需要查询的订单号是否正确

    TRADE_OVERDUE("TRADE_OVERDUE", "订单已经超过退款期限"),//订单已经超过可退款的最大期限(支付后一年内可退款)	请选择其他方式自行退款

    ERROR("ERROR", "订单已经超过退款期限"),//申请退款业务发生错误	该错误都会返回具体的错误原因，请根据实际返回做相应处理。

    USER_ACCOUNT_ABNORMAL("USER_ACCOUNT_ABNORMAL", "退款请求失败"),//用户帐号注销	此状态代表退款申请失败，商户可自行处理退款。

    INVALID_REQ_TOO_MUCH("INVALID_REQ_TOO_MUCH", "无效请求过多"),//连续错误请求数过多被系统短暂屏蔽	请检查业务是否正常，确认业务正常后请在1分钟后再来重试

    INVALID_TRANSACTIONID("INVALID_TRANSACTIONID", "无效transaction_id"),//请求参数未按指引进行填写	请求参数错误，检查原交易号是否存在或发起支付交易接口返回失败

    PARAM_ERROR("PARAM_ERROR", "参数错误"),//请求参数未按指引进行填写	请求参数错误，请重新检查再调用退款申请

    REFUNDNOTEXIST("REFUNDNOTEXIST", "退款订单查询失败"),//订单号错误或订单状态不正确	请检查订单号是否有误以及订单状态是否正确，如：未支付、已支付未退款

    NO_COMMENT("NO_COMMENT", "对应的时间段没有用户的评论数据"),//请查询其他时间段的评论数据
    TIME_EXPIRE("TIME_EXPIRE", "拉取的时间超过3个月"),//请拉取90天内的数据

    NOT_UTF8("NOT_UTF8", "编码格式错误");// 未使用指定编码格式	请使用UTF-8编码格式
    private static final Logger LOGGER = LoggerFactory.getLogger(WxPayStatusCode.class);

    private String code;
    private String msg;

    WxPayStatusCode(String code, String msg){
        this.code = code;
        this.msg = msg;
    }


    public static String getMsg(String code){
        for(WxPayStatusCode err : WxPayStatusCode.values()){
            if(err.code.equals(code)){
                return err.msg;
            }
        }
        return "errorCode not defined ";
    }

    public String getErrorCode(){
        return code;
    }

    public String getErrorMsg(){
        return msg;
    }
}
