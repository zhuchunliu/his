package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-08
 **/
@Data
public class DrugDayDto {

    @ApiModelProperty("药品编码")
    private String drugcode;

    @ApiModelProperty("药品名称")
    private String drugName;

    @ApiModelProperty("计价单位")
    private String unit;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品；4：诊断试剂")
    private String category;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("库存数量")
    private Double storeNum;

    @ApiModelProperty("销售数量")
    private Double salenum;

    @ApiModelProperty("利润")
    private Double profitFee;
}
