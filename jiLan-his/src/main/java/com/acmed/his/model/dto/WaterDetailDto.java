package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * WaterDetailDto
 * 收支明细
 * @author jimson
 * @date 2018/1/8
 */
@Data
public class WaterDetailDto {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("支付类型")
    private String feeType;
    @ApiModelProperty("来源 1挂号  2药品  3检查  4附加费")
    private String source;
    @ApiModelProperty("时间")
    private String  time;
    @ApiModelProperty("金额")
    private BigDecimal fee;
    @ApiModelProperty("收入   1收入  2支出")
    private Integer isIn;
    @ApiModelProperty("患者名字")
    private String patientName;
    @ApiModelProperty("年龄")
    private Integer age;
}
