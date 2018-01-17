package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * ShouzhiCountDto
 *
 * @author jimson
 * @date 2018/1/17
 */
@Data
public class ShouzhiCountDto {
    @ApiModelProperty("挂号收入")
    private BigDecimal applyMoney;
    @ApiModelProperty("药品收入")
    private BigDecimal drugMoney;
    @ApiModelProperty("检查收入")
    private BigDecimal prescriptionMoney;
    @ApiModelProperty("附加收入")
    private BigDecimal appendMoney;
    @ApiModelProperty("现金收入")
    private BigDecimal cashMoney;
    @ApiModelProperty("微信收入")
    private BigDecimal wxPayMoney;
    @ApiModelProperty("支付宝收入")
    private BigDecimal aliPayMoney;
    @ApiModelProperty("挂号退款")
    private BigDecimal applyRefundMoney;
    @ApiModelProperty("药品退款")
    private BigDecimal drugRefundMoney;
    @ApiModelProperty("检查退款")
    private BigDecimal prescriptionRefundMoney;
    @ApiModelProperty("附加退款")
    private BigDecimal appendRefundMoney;
    @ApiModelProperty("现金退款")
    private BigDecimal cashRefundMoney;
    @ApiModelProperty("微信退款")
    private BigDecimal wxPayRefundMoney;
    @ApiModelProperty("支付宝退款")
    private BigDecimal aliPayRefundMoney;

}
