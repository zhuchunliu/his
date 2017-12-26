package com.acmed.his.service;

import com.acmed.his.util.*;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.soecode.wxtools.api.IService;
import com.soecode.wxtools.api.WxService;
import com.soecode.wxtools.bean.WxJsapiConfig;
import com.soecode.wxtools.bean.result.WxOAuth2AccessTokenResult;
import com.soecode.wxtools.exception.WxErrorException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * WxManager
 * 微信相关的
 * @author jimson
 * @date 2017/11/22
 */
@Service
public class WxManager {
    /**
     * 实例化 统一业务API入口
     */
    private IService wxService = new WxService();

    private Logger logger = Logger.getLogger(WxManager.class);

    @Autowired
    private Environment environment;

    /**
     * 获取jssdk
     * @param url 参数
     * @return ResponseResult
     */
    public ResponseResult getJsSDk(String url){
        List<String> jsApiList = new ArrayList<>();
        //需要用到哪些JS SDK API 就设置哪些
        jsApiList.add("onMenuShareTimeline");
        jsApiList.add("onMenuShareAppMessage");
        jsApiList.add("onMenuShareQQ");
        jsApiList.add("onMenuShareWeibo");
        jsApiList.add("onMenuShareQZone");
        jsApiList.add("closeWindow");
        jsApiList.add("scanQRCode");
        try {
            //把config返回到前端进行js调用即可。
            WxJsapiConfig config = wxService.createJsapiConfig(url, jsApiList);
            return ResponseUtil.setSuccessResult();
        } catch (WxErrorException e) {
            logger.error(e.toString()+"获取jssdk失败");
            return ResponseUtil.setSuccessResult();
        }
    }

    /**
     * 获取opendid
     * @param code code
     * @return openid
     * @throws WxErrorException 异常
     */
    public String getOPenidByCode(String code) throws WxErrorException {
        WxOAuth2AccessTokenResult wxOAuth2AccessTokenResult = wxService.oauth2ToGetAccessToken(code);
        return wxOAuth2AccessTokenResult.getOpenid();
    }


    /**
     * 获取opendid
     * @param code code
     * @return openid
     * @throws WxErrorException 异常
     */
    public String getOpenid(String code) throws Exception{
        String url =  String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                environment.getProperty("weixin.appid"),environment.getProperty("weixin.secret"),code);
        String info = new RestTemplate().getForObject(url, String.class);
        JSONObject json = JSONObject.parseObject(info);

        return json.getString("openid");
    }


/*    字段名	变量名	必填	类型	示例值	描述
    返回状态码	return_code	是	String(16)	SUCCESS	SUCCESS/FAIL
    返回信息	return_msg	否	String(128)	签名失败
    返回信息，如非空，为错误原因
            签名失败
    参数格式校验错误
            以下字段在return_code为SUCCESS的时候有返回

    字段名	变量名	必填	类型	示例值	描述
    业务结果	result_code	是	String(16)	SUCCESS
    SUCCESS/FAIL
    SUCCESS退款申请接收成功，结果通过退款查询接口查询
    FAIL 提交业务失败
    错误代码	err_code	否	String(32)	SYSTEMERROR	列表详见错误码列表
    错误代码描述	err_code_des	否	String(128)	系统超时	结果信息描述
    公众账号ID	appid	是	String(32)	wx8888888888888888	微信分配的公众账号ID
    商户号	mch_id	是	String(32)	1900000109	微信支付分配的商户号
    随机字符串	nonce_str	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	随机字符串，不长于32位
    签名	sign	是	String(32)	5K8264ILTKCH16CQ2502SI8ZNMTM67VS	签名，详见签名算法
    微信订单号	transaction_id	是	String(32)	4007752501201407033233368018	微信订单号
    商户订单号	out_trade_no	是	String(32)	33368018	商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
    商户退款单号	out_refund_no	是	String(64)	121775250	商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
    微信退款单号	refund_id	是	String(32)	2007752501201407033233368018	微信退款单号
    退款金额	refund_fee	是	Int	100	退款总金额,单位为分,可以做部分退款
    应结退款金额	settlement_refund_fee	否	Int	100	去掉非充值代金券退款金额后的退款金额，退款金额=申请退款金额-非充值代金券退款金额，退款金额<=申请退款金额
    标价金额	total_fee	是	Int	100	订单总金额，单位为分，只能为整数，详见支付金额
    应结订单金额	settlement_total_fee	否	Int	100	去掉非充值代金券金额后的订单总金额，应结订单金额=订单金额-非充值代金券金额，应结订单金额<=订单金额。
    标价币种	fee_type	否	String(8)	CNY	订单金额货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    现金支付金额	cash_fee	是	Int	100	现金支付金额，单位为分，只能为整数，详见支付金额
    现金支付币种	cash_fee_type	否	String(16)	CNY	货币类型，符合ISO 4217标准的三位字母代码，默认人民币：CNY，其他值列表详见货币类型
    现金退款金额	cash_refund_fee	否	Int	100	现金退款金额，单位为分，只能为整数，详见支付金额
    代金券类型	coupon_type_$n	否	String(8)	CASH
    CASH--充值代金券
    NO_CASH---非充值代金券
    订单使用代金券时有返回（取值：CASH、NO_CASH）。$n为下标,从0开始编号，举例：coupon_type_0
    代金券退款总金额	coupon_refund_fee	否	Int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
    单个代金券退款金额	coupon_refund_fee_$n	否	Int	100	代金券退款金额<=退款金额，退款金额-代金券或立减优惠退款金额为现金，说明详见代金券或立减优惠
    退款代金券使用数量	coupon_refund_count	否	Int	1	退款代金券使用数量
    退款代金券ID	coupon_refund_id_$n	否	String(20)	10000 	退款代金券ID, $n为下标，从0开始编号*/
    /**
     * 退款
     * @param orderCode 订单
     * @param fee 退款金额
     * @param reason 原因
     * @param totalFee 总金额
     * @return map
     * @throws Exception
     */
    public Map<String, String> refund(String orderCode,String fee,String reason,String totalFee) throws Exception {
        String mchId = environment.getProperty("weixin.mchId");
        Map<String,String> param = new HashMap<>(15);
        param.put("appid",environment.getProperty("weixin.appid"));
        param.put("mch_id",mchId);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign_type", WXPayConstants.MD5);
        param.put("total_fee", totalFee);// 订单金额
        param.put("refund_fee", fee);// 退款金额
        param.put("refund_desc", reason);// 退款原因
        param.put("out_trade_no",orderCode);
        param.put("out_refund_no",WaterCodeUtil.getWaterCode());//退款单号
        String s = WXPayUtil.generateSignature(param, environment.getProperty("weixin.key"));
        param.put("sign",s);
        String xml = WXPayUtil.mapToXml(param);
        String s1 = WXPayRequest.postXmlWithKey(WXPayConstants.REFUND_URL_SUFFIX,xml,mchId);
        Map<String, String> map = WXPayUtil.xmlToMap(s1);
        System.err.println(map);
        return map;
    }

}
