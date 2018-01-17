package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;


/**
 * WaterDetailVo
 *
 * @author jimson
 * @date 2018/1/8
 */
@Data
public class WaterDetailVo {
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("支付类型")
    private String feeTypeStr;
    @ApiModelProperty("时间")
    private String  time;
    @ApiModelProperty("金额")
    private String balanceStr;
    @ApiModelProperty("收入   1收入  2支出")
    private Integer isIn;
    @ApiModelProperty("收入   1收入  2支出")
    private String isInStr;
    @ApiModelProperty("支付类型")
    private String feeType;
    @ApiModelProperty("来源 1挂号  2药品  3检查  4附加费")
    private String source;
    @ApiModelProperty("金额")
    private BigDecimal fee;
    @ApiModelProperty("患者名字")
    private String patientName;
    @ApiModelProperty("年龄")
    private Integer age;
}
