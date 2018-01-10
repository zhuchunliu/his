package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * WorkloadDayAndTotalDto
 *
 * @author jimson
 * @date 2018/1/10
 */
@Data
public class WorkloadDayAndTotalDto {
    @ApiModelProperty("日预约量")
    private Integer applyNumDay;
    @ApiModelProperty("净总收入")
    private BigDecimal incomeFeeTotal;
    @ApiModelProperty("挂号总收入")
    private BigDecimal applyFeeTotal;
    @ApiModelProperty("挂号总退款")
    private BigDecimal applyRefundFeeTotal;
}
