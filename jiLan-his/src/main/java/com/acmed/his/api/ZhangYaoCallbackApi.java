package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.pojo.zy.ZYExpressCallbackMo;
import com.acmed.his.pojo.zy.ZYPayCallbackMo;
import com.acmed.his.pojo.zy.ZYRefundCallbackMo;
import com.acmed.his.service.ZhangYaoCallbackManager;
import com.acmed.his.support.WithoutToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Darren on 2018-06-07
 **/
@Api(tags = "掌药-回调")
@RequestMapping("/feedback")
@RestController
public class ZhangYaoCallbackApi {

    private Logger logger = LoggerFactory.getLogger(ZhangYaoReceiveApi.class);

    @Autowired
    private ZhangYaoCallbackManager callbackManager;

    @ApiOperation(value = "发货回调")
    @PostMapping("/express")
    @WithoutToken
    public ResponseResult express(@RequestBody ZYExpressCallbackMo mo){
        logger.info("express callback info : "+mo);
        if(StringUtils.isEmpty(mo.getExpressNo())){
            return ResponseUtil.setParamEmptyError("expressNo");
        }
        if(StringUtils.isEmpty(mo.getOrderId())){
            return ResponseUtil.setParamEmptyError("orderId");
        }
        callbackManager.express(mo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "付费回调")
    @PostMapping("/pay")
    @WithoutToken
    public ResponseResult pay(@RequestBody ZYPayCallbackMo mo){
        logger.info("pay callback info : "+mo);
        if(null == mo.getOrderIds() || 0 == mo.getOrderIds().length){
            return ResponseUtil.setParamEmptyError("orderIds");
        }
        if(null == mo.getPayStatus()){
            return ResponseUtil.setParamEmptyError("payStatus");
        }
        if(mo.getPayStatus() != 1 && mo.getPayStatus() != 0 ){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"payStatus只能为0和1");
        }
        callbackManager.updateZyOrderPayStatus(mo);
        new Thread(() -> {
            callbackManager.pushMsg(mo);
        }).start();

        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "退款回调")
    @PostMapping("/refund")
    @WithoutToken
    public ResponseResult refund(@RequestBody ZYRefundCallbackMo mo){
        logger.info("refund callback info : "+mo);
        if(StringUtils.isEmpty(mo.getOrderId())){
            return ResponseUtil.setParamEmptyError("orderId");
        }
        if(null == mo.getRefundStatus()){
            return ResponseUtil.setParamEmptyError("refundStatus");
        }
        if(mo.getRefundStatus() != 1 && mo.getRefundStatus() != 0 ){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"refundStatus只能为0和1");
        }
        callbackManager.updateZyOrderRefundStatus(mo);

        return ResponseUtil.setSuccessResult();
    }

    /**
     * 返回掌药支付结果
     * @param info
     * @return
     * @throws Exception
     */
    @SendToUser(value = "/information")
    public String information(String info) {
        System.err.println(info);//支付结果
        return info;
    }

}
