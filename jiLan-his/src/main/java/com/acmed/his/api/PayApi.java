package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.*;
import com.acmed.his.service.*;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.*;

import com.alibaba.druid.support.json.JSONUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
@Api(tags = "支付管理")
public class PayApi {

    @Autowired
    private Environment environment;

    @Autowired
    private AccompanyingOrderManager accompanyingOrderManager;

    @Autowired
    private PatientManager patientManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private PayManager payManager;

    @Autowired
    private CommonManager commonManager;

    @Autowired
    private ApplyManager applyManager;

    @Autowired
    private InsuranceOrderManager insuranceOrderManager;

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
        param.put("out_trade_no",orderCode+"");
        param.put("total_fee", totalBalance.multiply(new BigDecimal(100)).intValue()+"");
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
        if ("SUCCESS".equals(map.get("result_code"))){
            Map<String,String> result = new HashMap<>();
            result.put("appId",map.get("appid"));
            result.put("timeStamp",WXPayUtil.getCurrentTimestamp()+"");
            result.put("nonceStr", UUIDUtil.generate32());
            result.put("package","prepay_id="+map.get("prepay_id"));
            result.put("signType","MD5");
            String s2 = WXPayUtil.generateSignature(result, environment.getProperty("weixin.key"));
            result.put("paySign",s2);
            JSONUtils.toJSONString(result);
            System.err.println();
            return ResponseUtil.setSuccessResult(JSONUtils.toJSONString(result));
        }else {
            logger.error("微信支付初始化异常--------"+map.toString());
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PAY_INIT_ERR,"网络繁忙");
        }
    }

    @ApiOperation("就医北上广回调")
    @PostMapping("BSGCallBack")
    @WithoutToken
    @Transactional
    public void BSGCallBack(HttpServletRequest req, HttpServletResponse resp){
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "application/xml;charset=UTF-8");
            String resString = WXPayUtil.parseRequst(req);
            logger.info("回调通知内容"+resString);
            String respString = "FAIL";
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = WXPayUtil.toMap(resString.getBytes(), "utf-8");
                boolean s = WXPayUtil.isSignatureValid(map,environment.getProperty("weixin.key"));
                if (!s){
                    respString = "FAIL";
                }else {
                    String return_code = map.get("return_code");
                    String result_code = map.get("result_code");
                    if (StringUtils.equals("SUCCESS", return_code) && StringUtils.equals("SUCCESS", result_code)) {
                        // 本地訂單號
                        String out_trade_no = map.get("out_trade_no");
                        // 第三方订单号
                        String transaction_id = map.get("transaction_id");
                        logger.info("微信支付订单号" + transaction_id);
                        AccompanyingOrder byOrderCode = accompanyingOrderManager.getByOrderCode(out_trade_no);
                        if (byOrderCode != null) {
                            if (byOrderCode.getPayStatus()==0){
                                AccompanyingOrder accompanyingOrder = new AccompanyingOrder();
                                accompanyingOrder.setOrderCode(out_trade_no);
                                accompanyingOrder.setPayStatus(1);
                                accompanyingOrder.setStatus(2);
                                accompanyingOrder.setPayType(1);
                                accompanyingOrder.setOtherOrderCode(transaction_id);
                                int update = accompanyingOrderManager.update(accompanyingOrder);
                                if (update == 1) {
                                    // 回调成功
                                    respString = "SUCCESS";
                                }
                            }
                        }
                    }
                }
            }
            resp.getWriter().write("<xml><return_code><![CDATA["+respString+"]]></return_code></xml>");
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }


    @ApiOperation("挂号支付初始化")
    @GetMapping("applyInit")
    @ResponseBody
    public ResponseResult applyInit(@RequestParam("id") String id, @AccessToken AccessInfo info, HttpServletRequest request) throws Exception {
        Apply applyById = applyManager.getApplyById(id);
        if (applyById == null){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER,"挂号单不存在");
        }
        if (StringUtils.isNotEmpty(applyById.getIsPaid()) && StringUtils.equals("1",applyById.getIsPaid()) ){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_IS_PAY,"请不要重复支付");
        }


        Patient patientById = patientManager.getPatientById(info.getPatientId());

        BigDecimal totalBalance = BigDecimal.valueOf(applyById.getFee());
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
        param.put("out_trade_no",applyById.getId());
        param.put("total_fee", totalBalance.multiply(new BigDecimal(100)).intValue()+"");
        // 客户端ip
        param.put("spbill_create_ip",request.getRemoteAddr());
        param.put("notify_url",environment.getProperty("weixin.url")+"/pay/applyCallBack");
        param.put("trade_type","JSAPI");
        param.put("openid",openid);
        // 生成签名
        String s = WXPayUtil.generateSignature(param, environment.getProperty("weixin.key"));
        param.put("sign",s);
        String xml = WXPayUtil.mapToXml(param);
        String s1 = WXPayRequest.postXml(WXPayConstants.UNIFIEDORDER_URL_SUFFIX,xml);
        Map<String, String> map = WXPayUtil.xmlToMap(s1);
        System.err.println(map);
        if ("SUCCESS".equals(map.get("result_code"))){
            Map<String,String> result = new HashMap<>();
            result.put("appId",map.get("appid"));
            result.put("timeStamp",WXPayUtil.getCurrentTimestamp()+"");
            result.put("nonceStr", UUIDUtil.generate32());
            result.put("package","prepay_id="+map.get("prepay_id"));
            result.put("signType","MD5");
            String s2 = WXPayUtil.generateSignature(result, environment.getProperty("weixin.key"));
            result.put("paySign",s2);
            JSONUtils.toJSONString(result);
            System.err.println();
            return ResponseUtil.setSuccessResult(JSONUtils.toJSONString(result));
        }else {
            logger.error("微信支付初始化异常--------"+map.toString());
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PAY_INIT_ERR,"网络繁忙");
        }
    }



    @ApiOperation("挂号回调")
    @PostMapping("applyCallBack")
    @WithoutToken
    @Transactional
    public void applyCallBack(HttpServletRequest req, HttpServletResponse resp){
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "application/xml;charset=UTF-8");
            String resString = WXPayUtil.parseRequst(req);
            logger.info("回调通知内容"+resString);
            String respString = "FAIL";
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = WXPayUtil.toMap(resString.getBytes(), "utf-8");
                boolean s = WXPayUtil.isSignatureValid(map,environment.getProperty("weixin.key"));
                if (!s){
                    respString = "FAIL";
                }else {
                    String return_code = map.get("return_code");
                    String result_code = map.get("result_code");
                    if (StringUtils.equals("SUCCESS", return_code) && StringUtils.equals("SUCCESS", result_code)) {
                        // 本地訂單號
                        String out_trade_no = map.get("out_trade_no");
                        // 第三方订单号
                        String transaction_id = map.get("transaction_id");
                        logger.info("微信支付订单号" + transaction_id);
                        Apply applyById = applyManager.getApplyById(out_trade_no);
                        if (applyById != null) {
                            if (StringUtils.isEmpty(applyById.getIsPaid()) || StringUtils.equals("0",applyById.getIsPaid())){
                                Apply apply = new Apply();
                                apply.setId(out_trade_no);
                                apply.setClinicNo(commonManager.getFormatVal(apply.getOrgCode() + "applyCode", "000000000"));
                                apply.setIsPaid("1");
                                int i = applyManager.updateApply(apply);
                                PayStatements payStatements = new PayStatements();
                                payStatements.setId(UUIDUtil.generate32());
                                payStatements.setFeeType("2");
                                payStatements.setOrgCode(applyById.getOrgCode());
                                payStatements.setSource("1");
                                payStatements.setApplyId(out_trade_no);
                                payStatements.setPatientId(applyById.getPatientId());
                                payStatements.setPayId(transaction_id);
                                payStatements.setFee(BigDecimal.valueOf(applyById.getFee()));
                                payStatements.setPayStatus("1");
                                payManager.addPayStatements(payStatements);
                                if (i == 1) {
                                    // 回调成功
                                    respString = "SUCCESS";
                                }
                            }
                        }
                    }
                }
            }
            resp.getWriter().write("<xml><return_code><![CDATA["+respString+"]]></return_code></xml>");
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }


    @ApiOperation("保单支付初始化")
    @GetMapping("insuranceOrderInit")
    @ResponseBody
    public ResponseResult insuranceOrderInit(@RequestParam("id") String id, @AccessToken AccessInfo info, HttpServletRequest request) throws Exception {
        InsuranceOrder insuranceOrder = insuranceOrderManager.getById(id);
        if (insuranceOrder == null){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_ORDER,"挂号单不存在");
        }
        if (insuranceOrder.getFee().equals(0) ||  StringUtils.isNotEmpty(insuranceOrder.getPayId()) ){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_IS_PAY,"请不要重复支付");
        }
        Integer userId = info.getUserId();
        if (!insuranceOrder.getUserId().equals(userId)){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_IS_PAY,"暂时不支持代付功能");
        }
        User userDetail = userManager.getUserDetail(userId);
        String openid = userDetail.getOpenid();
        String mchId = environment.getProperty("weixin.mchId");
        Map<String,String> param = new HashMap<>(15);
        param.put("appid",environment.getProperty("weixin.appid"));
        param.put("mch_id",mchId);
        param.put("nonce_str", WXPayUtil.generateNonceStr());
        param.put("sign_type", WXPayConstants.MD5);
        param.put("body","机构保单支付");
        //param.put("detail","详情");
        long currentTimestamp = WXPayUtil.getCurrentTimestamp();
        System.err.println(currentTimestamp);
        param.put("out_trade_no",id);
        param.put("total_fee", insuranceOrder.getFee().toString());
        // 客户端ip
        param.put("spbill_create_ip",request.getRemoteAddr());
        param.put("notify_url",environment.getProperty("weixin.url")+"/pay/insuranceOrderCallBack");
        param.put("trade_type","JSAPI");
        param.put("openid",openid);
        // 生成签名
        String s = WXPayUtil.generateSignature(param, environment.getProperty("weixin.key"));
        param.put("sign",s);
        String xml = WXPayUtil.mapToXml(param);
        String s1 = WXPayRequest.postXml(WXPayConstants.UNIFIEDORDER_URL_SUFFIX,xml);
        Map<String, String> map = WXPayUtil.xmlToMap(s1);
        System.err.println(map);
        if ("SUCCESS".equals(map.get("result_code"))){
            Map<String,String> result = new HashMap<>();
            result.put("appId",map.get("appid"));
            result.put("timeStamp",WXPayUtil.getCurrentTimestamp()+"");
            result.put("nonceStr", UUIDUtil.generate32());
            result.put("package","prepay_id="+map.get("prepay_id"));
            result.put("signType","MD5");
            String s2 = WXPayUtil.generateSignature(result, environment.getProperty("weixin.key"));
            result.put("paySign",s2);
            JSONUtils.toJSONString(result);
            System.err.println();
            return ResponseUtil.setSuccessResult(JSONUtils.toJSONString(result));
        }else {
            logger.error("微信支付初始化异常--------"+map.toString());
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PAY_INIT_ERR,"网络繁忙");
        }
    }



    @ApiOperation("保单回调")
    @PostMapping("insuranceOrderCallBack")
    @WithoutToken
    @Transactional
    public void insuranceOrderCallBack(HttpServletRequest req, HttpServletResponse resp){
        try {
            req.setCharacterEncoding("utf-8");
            resp.setCharacterEncoding("utf-8");
            resp.setHeader("Content-type", "application/xml;charset=UTF-8");
            String resString = WXPayUtil.parseRequst(req);
            logger.info("回调通知内容"+resString);
            String respString = "FAIL";
            if(resString != null && !"".equals(resString)){
                Map<String,String> map = WXPayUtil.toMap(resString.getBytes(), "utf-8");
                boolean s = WXPayUtil.isSignatureValid(map,environment.getProperty("weixin.key"));
                if (!s){
                    respString = "FAIL";
                }else {
                    String return_code = map.get("return_code");
                    String result_code = map.get("result_code");
                    if (StringUtils.equals("SUCCESS", return_code) && StringUtils.equals("SUCCESS", result_code)) {
                        // 本地訂單號
                        String out_trade_no = map.get("out_trade_no");
                        // 第三方订单号
                        String transaction_id = map.get("transaction_id");
                        logger.info("微信支付订单号" + transaction_id);
                        InsuranceOrder insuranceOrder = insuranceOrderManager.getById(out_trade_no);
                        if (insuranceOrder != null) {
                            if (StringUtils.isEmpty(insuranceOrder.getPayId())){
                                insuranceOrder.setFeeType("2");
                                insuranceOrder.setPayId(transaction_id);
                                int i = insuranceOrderManager.update(insuranceOrder);
                                if (i == 1) {
                                    // 回调成功
                                    respString = "SUCCESS";
                                }
                            }
                        }
                    }
                }
            }
            resp.getWriter().write("<xml><return_code><![CDATA["+respString+"]]></return_code></xml>");
        } catch (Exception e) {
            logger.error(e.toString());
            e.printStackTrace();
        }
    }
}
