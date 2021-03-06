package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * WaterDay
 * 报表
 * @author jimson
 * @date 2018/1/5
 */
@Data
@Table(name = "t_r_water_day")
@NameStyle
public class WaterDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("机构编码")
    private Integer orgCode;

    @ApiModelProperty("报表日期")
    private String date;

    @ApiModelProperty("微信支付金额")
    private BigDecimal wxPayMoney;

    @ApiModelProperty("微信支付次数")
    private Integer wxPayNum;

    @ApiModelProperty("支付宝支付金额")
    private BigDecimal aliPayMoney;

    @ApiModelProperty("支付宝支付次数")
    private Integer aliPayNum;

    @ApiModelProperty("现金支付金额")
    private BigDecimal cash;

    @ApiModelProperty("现金支付次数")
    private Integer cashNum;

    @ApiModelProperty("挂号金额")
    private BigDecimal registeredMoney;

    @ApiModelProperty("挂号次数")
    private Integer registeredNum;

    @ApiModelProperty("检查金额")
    private BigDecimal prescriptionMoney;

    @ApiModelProperty("检查次数")
    private Integer prescriptionNum;

    @ApiModelProperty("总支付金额")
    private BigDecimal payMoney;

    @ApiModelProperty("总支付次数")
    private Integer payNum;

    @ApiModelProperty("退款金额")
    private BigDecimal refundMoney;

    @ApiModelProperty("退款次数")
    private Integer refundNum;

    @ApiModelProperty("挂号退款金额")
    private BigDecimal registeredRefundMoney;

    @ApiModelProperty("挂号退款次数")
    private Integer registeredRefundNum;

    @ApiModelProperty("检查退款金额")
    private BigDecimal prescriptionRefundMoney;

    @ApiModelProperty("检查退款次数")
    private Integer prescriptionRefundNum;

    @ApiModelProperty("药支付金额")
    private BigDecimal drugMoney;

    @ApiModelProperty("药支付次数")
    private Integer drugNum;

    @ApiModelProperty("药退款金额")
    private BigDecimal drugRefundMoney;

    @ApiModelProperty("药退款次数")
    private Integer drugRefundNum;

    @ApiModelProperty("附加费支付金额")
    private BigDecimal appendMoney;

    @ApiModelProperty("附加费支付次数")
    private Integer appendNum;

    @ApiModelProperty("附加费退款金额")
    private BigDecimal appendRefundMoney;

    @ApiModelProperty("附加费退款次数")
    private Integer appendRefundNum;

    @ApiModelProperty("微信退款金额")
    private BigDecimal wxPayRefundMoney;

    @ApiModelProperty("微信退款次数")
    private Integer wxPayRefundNum;

    @ApiModelProperty("支付宝退款金额")
    private BigDecimal aliPayRefundMoney;

    @ApiModelProperty("支付宝退款次数")
    private Integer aliPayRefundNum;

    @ApiModelProperty("现金退款金额")
    private BigDecimal cashRefundMoney;

    @ApiModelProperty("现金退款次数")
    private Integer cashRefundNum;
}
