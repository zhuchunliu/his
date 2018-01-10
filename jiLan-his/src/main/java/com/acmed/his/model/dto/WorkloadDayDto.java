package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-10
 **/
@Data
public class WorkloadDayDto {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("就诊量")
    private Integer diagnoseNum;

    @ApiModelProperty("取消量")
    private Integer cancelNum;

    @ApiModelProperty("违约量")
    private Integer breachNum;

    @ApiModelProperty("总预约量")
    private Integer applyNum;

    @ApiModelProperty("处方量")
    private Integer prescriptionNum;

    @ApiModelProperty("净收入,挂号费+处方费-退款费")
    private Double incomeFee;

    @ApiModelProperty("挂号金额")
    private Double applyFee;

    @ApiModelProperty("处方金额 包含：药品/检查/附加")
    private Double prescriptionFee;

    @ApiModelProperty("药品金额")
    private Double drugFee;

    @ApiModelProperty("检查金额")
    private Double inspectFee;

    @ApiModelProperty("附加金额")
    private Double chargeFee;

    @ApiModelProperty("挂号退款金额")
    private Double applyRefundFee;

    @ApiModelProperty("处方退款金额  包含：药品/检查/附加")
    private Double prescriptionRefundFee;

    @ApiModelProperty("药品退款金额")
    private Double drugRefundFee;

    @ApiModelProperty("检查退款金额")
    private Double inspectRefundFee;

    @ApiModelProperty("附加退款金额")
    private Double chargeRefundFee;
}
