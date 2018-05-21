package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-04
 **/
@Data
public class DrugStockDto {

    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("计价单位")
    private String unit;

    @ApiModelProperty("生产厂家")
    private String manufacturerName;

    @ApiModelProperty("库存数量")
    private Double num;

    @ApiModelProperty("批发价")
    private Double bid;

    @ApiModelProperty("处方价")
    private Double retailPrice;

    @ApiModelProperty("批发额")
    private Double bidFee;

    @ApiModelProperty("处方额")
    private Double retailFee;

}
