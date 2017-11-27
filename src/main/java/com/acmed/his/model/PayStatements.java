package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * PayStatements
 * 支付表
 * @author jimson
 * @date 2017/11/23
 */
@Data
@Table(name = "t_b_pay_statements")
@NameStyle
public class PayStatements {
    @Id
    @ApiModelProperty("支付流水号")
    private String id;

    @ApiModelProperty("付费类型")
    private String feeType;

    @ApiModelProperty("来源")
    private String source;

    @ApiModelProperty("处方id")
    private Integer prescriptionId;

    @ApiModelProperty("挂号单号id")
    private Integer applyId;

    @ApiModelProperty("患者id")
    private Integer patientId;

    @ApiModelProperty("付款账号")
    private String payerAccount;

    @ApiModelProperty("第三方支付订单号")
    private String payId;

    @ApiModelProperty("支付费用")
    private BigDecimal fee;

    @ApiModelProperty("支付状态")
    private String payStatus;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("修改时间")
    private Date modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
