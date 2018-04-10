package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-01-24
 **/
@Data
public class DispensingFeeSurveyVo {

    public DispensingFeeSurveyVo(Double fee,List<Map<String, Object>> payList, List<Map<String, Object>> refundList) {
        this.fee = Optional.ofNullable(fee).map(Double::doubleValue).orElse(0d);
        if(null != payList){
            for(Map<String,Object> pay : payList){
                switch (Integer.parseInt(pay.get("feetype").toString())){
                    case 0:
                        this.cashFee = new Double(pay.get("fee").toString()).doubleValue();
                        break;
                    case 1:
                        this.weixinFee = new Double(pay.get("fee").toString()).doubleValue();
                        break;
                    case 2:
                        this.alipayFee = new Double(pay.get("fee").toString()).doubleValue();
                        break;
                    case 3:
                        this.cardFee = new Double(pay.get("fee").toString()).doubleValue();
                        break;
                }
            }

        }
        if(null != refundList){
            for(Map<String,Object> refund : refundList){
                switch (Integer.parseInt(refund.get("feetype").toString())){
                    case 0:
                        this.cashRefundFee = new Double(refund.get("fee").toString()).doubleValue();
                        break;
                    case 1:
                        this.weixinRefundFee = new Double(refund.get("fee").toString()).doubleValue();
                        break;
                    case 2:
                        this.alipayRefundFee = new Double(refund.get("fee").toString()).doubleValue();
                        break;
                    case 3:
                        this.cardRefundFee = new Double(refund.get("fee").toString()).doubleValue();
                        break;
                }
            }
        }
        this.totalFee = this.cashFee + this.weixinFee + this.alipayFee+this.cardFee;
        this.totalRefundFee = this.cashRefundFee + this.weixinRefundFee + this.alipayRefundFee+this.cardRefundFee;
    }

    @ApiModelProperty("总费用：收过费+未收费+已退款总额")
    private double fee;

    @ApiModelProperty("实收总额")
    private double totalFee;

    @ApiModelProperty("现金实收额")
    private double cashFee;

    @ApiModelProperty("微信实收额")
    private double weixinFee;

    @ApiModelProperty("支付宝实收额")
    private double alipayFee;

    @ApiModelProperty("刷卡实收额")
    private double cardFee;

    @ApiModelProperty("实退总额")
    private double totalRefundFee;

    @ApiModelProperty("现金实退额")
    private double cashRefundFee;

    @ApiModelProperty("微信实退额")
    private double weixinRefundFee;

    @ApiModelProperty("支付宝实退额")
    private double alipayRefundFee;

    @ApiModelProperty("刷卡实收额")
    private double cardRefundFee;


}
