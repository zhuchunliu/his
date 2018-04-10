package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * PayRefuse
 *
 * @author jimson
 * @date 2017/11/23
 */
@Data
@Table(name = "t_b_pay_refuse")
@NameStyle
public class PayRefuse {
    @Id
    @ApiModelProperty("退费流水号")
    private String id;

    @ApiModelProperty("机构编码")
    private Integer orgCode;

    @ApiModelProperty("付费类型 现金 支付宝 微信 刷卡")
    private String feeType;

    @ApiModelProperty("来源  1:挂号  2:药  3：检查  4：附加")
    private String source;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("挂号单号id")
    private String applyId;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("退款明细 药：item，检查：inspect，附加：charge")
    private String itemId;

    @ApiModelProperty("付款账号")
    private String payerAccount;

    @ApiModelProperty("第三方支付订单号")
    private String payId;

    @ApiModelProperty("第三方退费订单号")
    private String refuseId;

    @ApiModelProperty("支付费用")
    private BigDecimal fee;

    @ApiModelProperty("退费状态")
    private String refuseStatus;

    @ApiModelProperty("退款理由")
    private String reason;

    @ApiModelProperty("退款说明")
    private String state;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
