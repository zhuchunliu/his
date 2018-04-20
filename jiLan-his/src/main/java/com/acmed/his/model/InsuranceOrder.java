package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * InsuranceOrder
 * 保险单
 * @author jimson
 * @date 2018/4/19
 */
@Data
@NameStyle
@Table(name = "tb_insurance_order")
public class InsuranceOrder {
    @Id
    @ApiModelProperty("支付流水号 32位")
    private String id;

    @ApiModelProperty("付费类型 现金 支付宝 微信 刷卡")
    private String feeType;

    @ApiModelProperty("机构编码")
    private Integer orgCode;

    @ApiModelProperty("第三方支付订单号")
    private String payId;

    @ApiModelProperty("支付费用分")
    private Integer fee;

    @ApiModelProperty("挂号单数量")
    private Integer applyNum;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("预约时间")
    private String appointmentTime;

    @ApiModelProperty("用户id")
    private Integer userId;
}
