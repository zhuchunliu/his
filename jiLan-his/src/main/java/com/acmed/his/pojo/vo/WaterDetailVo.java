package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


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
}
