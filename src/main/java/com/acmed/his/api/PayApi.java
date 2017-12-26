package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.AccompanyingOrder;
import com.acmed.his.model.Patient;
import com.acmed.his.service.AccompanyingOrderManager;
import com.acmed.his.service.PatientManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.*;

import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * PayApi
 *
 * @author jimson
 * @date 2017/12/25
 */
@Controller
@RequestMapping("pay")
public class PayApi {

    @Autowired
    private Environment environment;

    @Autowired
    private AccompanyingOrderManager accompanyingOrderManager;

    @Autowired
    private PatientManager patientManager;
    private static Logger logger = Logger.getLogger(PayApi.class);

    @ApiOperation("就医北上广初始化支付")
    @GetMapping("payBSGInit")
    @ResponseBody
    public ResponseResult payBSGInit(@RequestParam("orderCode") String orderCode, @AccessToken AccessInfo info, HttpServletRequest request) throws Exception {
        Patient patientById = patientManager.getPatientById(info.getPatientId());
        AccompanyingOrder byOrderCode = accompanyingOrderManager.getByOrderCode(orderCode);
        if (byOrderCode == null){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER,"订单不存在");
        }
        Integer payStatus = byOrderCode.getPayStatus();
        if (payStatus !=0){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_IS_PAY,"订单已经支付");
        }
        BigDecimal totalBalance = byOrderCode.getTotalBalance();
        String openid = patientById.getOpenid();
        String mchId = environment.getProperty("weixin.mchId");
        Map<String,String> param = new HashMap<>(15);
        param.put("appid",environment.getProperty("weixin.appid"));
        param.put("mch_id",mchId);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign_type", WXPayConstants.MD5);
        param.put("body","预约挂号");
        //param.put("detail","详情");
        long currentTimestamp = WXPayUtil.getCurrentTimestamp();
        System.err.println(currentTimestamp);
        param.put("out_trade_no",currentTimestamp+"");
        param.put("total_fee", totalBalance.multiply(new BigDecimal(100)).toString());
        // 客户端ip
        param.put("spbill_create_ip",request.getRemoteAddr());
        param.put("notify_url",environment.getProperty("weixin.url")+"/pay/BSGCallBack");
        param.put("trade_type","JSAPI");
        param.put("openid",openid);
        // 生成签名
        String s = WXPayUtil.generateSignature(param, environment.getProperty("weixin.key"));
        param.put("sign",s);
        String xml = WXPayUtil.mapToXml(param);
        String s1 = WXPayRequest.postXml(WXPayConstants.UNIFIEDORDER_URL_SUFFIX,xml);
        Map<String, String> map = WXPayUtil.xmlToMap(s1);
        System.err.println(map);
        return ResponseUtil.setSuccessResult(map);
    }

    @ApiOperation("就医北上广回调")
    @RequestMapping("BSGCallBack")
    @WithoutToken
    public void BSGCallBack(HttpServletRequest req, HttpServletResponse resp){
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "text/html;charset=UTF-8");
            String resString = WXPayUtil.parseRequst(req);
            System.out.println("通知内容：" + resString);
            String respString = "fail";
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = WXPayUtil.toMap(resString.getBytes(), "utf-8");
                boolean s = WXPayUtil.isSignatureValid(map,environment.getProperty("weixin.key"));
                if (!s){
                    respString = "fail";
                }else {
                    String status = map.get("status");
                    if(status != null && "0".equals(status)){
                        String result_code = map.get("result_code");
                        if(result_code != null && "0".equals(result_code)){
                            // 本地訂單號
                            String out_trade_no = map.get("out_trade_no");
                            // 第三方订单号
                            String transaction_id = map.get("transaction_id");
                            logger.info("微信支付订单号"+transaction_id);
                            AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
                            accompanyingOrder.setOrderCode(out_trade_no);
                            accompanyingOrder.setPayStatus(0);
                            AccompanyingOrder byOrderCode = accompanyingOrderManager.getByOrderCode(out_trade_no);
                            if (byOrderCode != null){
                                accompanyingOrder.setStatus(2);
                                accompanyingOrder.setPayType(1);
                                accompanyingOrder.setOrderCode(transaction_id);
                                int update = accompanyingOrderManager.update(accompanyingOrder);
                                if (update == 1){
                                    // 回调成功
                                    respString = "success";
                                }
                            }
                        }
                    }
                }
            }
            resp.getWriter().write(respString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
