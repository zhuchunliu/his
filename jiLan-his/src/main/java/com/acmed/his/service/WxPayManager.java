package com.acmed.his.service;


import com.acmed.his.config.MyWXPayConfig;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

/**
 * WxPayManager
 *
 * @author jimson
 * @date 2017/12/6
 */
@Service
public class WxPayManager {

    /**
     * 統一下单   带* 为必填
     * @param body *商品简单描述 例如 腾讯充值中心-QQ会员充值
     * @param openid *用戶openid
     * @param outTradeNo *商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|*@ ，且在同一个商户号下唯一。
     * @param deviceInfo 自定义参数，可以为终端设备号(门店号或收银设备ID)，PC网页或公众号内支付可以传"WEB"
     * @param totalFee *订单总金额，单位为分
     * @param spbillCreateIp *APP和网页支付提交用户端ip，Native支付填调用微信支付API的机器IP。
     * @param tradeType 交易类型
     *                  JSAPI--公众号支付、NATIVE--原生扫码支付、APP--app支付，统一下单接口trade_type的传参可参考这里
     *                  MICROPAY--刷卡支付，刷卡支付有单独的支付接口，不调用统一下单接口
     * @param productId trade_type=NATIVE时（即扫码支付），此参数必传。此参数为二维码中包含的商品ID，商户自行定义。
     * @throws Exception
     */
    public Map<String, String> unifiedorder(String body,String openid,String outTradeNo,String deviceInfo,Integer totalFee,String spbillCreateIp,String tradeType,String productId) throws Exception {
        WXPay wxpay=new WXPay(MyWXPayConfig.getInstance());
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("body", body);
        data.put("openid", openid);
        data.put("out_trade_no", outTradeNo);
        if (StringUtils.isNotEmpty(deviceInfo)){
            data.put("device_info", deviceInfo);
        }
        data.put("total_fee", totalFee.toString());
        data.put("spbill_create_ip", spbillCreateIp);
        data.put("notify_url", "http://test.letiantian.me/wxpay/notify");
        data.put("trade_type", tradeType);
        if (StringUtils.isNotEmpty(productId)){
            data.put("product_id", productId);
        }
        Map<String, String> map = wxpay.unifiedOrder(data);
        String return_code = map.get("return_code");
        String result_code = map.get("result_code");
        if (StringUtils.equals(result_code,"SUCCESS") && StringUtils.equals(return_code,"SUCCESS")){
            TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
            long l = System.currentTimeMillis() / 1000;

            String timeStamp = l+"";// 时间戳
            String appId = map.get("appid");//appid
            String nonceStr = WXPayUtil.generateNonceStr();// 随机数
            String prepayId ="prepay_id" + map.get("prepay_id");//
            String signType = "MD5";// 加密方式
            // 签名
            HashMap<String,String> signMapParam = new HashMap<>();

        }
        return map;
    }
}
