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

    @ApiModelProperty("小单位 字典表： MinUnit")
    private String minUnit;

    @ApiModelProperty("剂量单位 字典表：DoseUnit")
    private String doseUnit;

    @ApiModelProperty("生产厂家")
    private String manufacturerName;

    @ApiModelProperty("库存数量")
    private Double num;

    @ApiModelProperty("批发价")
    private String bid;

    @ApiModelProperty("处方价,大单位")
    private Double retailPrice;

    @ApiModelProperty("处方价,大单位")
    private String retailPriceName;

    @ApiModelProperty("小单位零售价")
    private Double minRetailPrice;

    @ApiModelProperty("小单位零售价")
    private String minRetailPriceName;

    @ApiModelProperty("小单位零售价对应单位  1：小单位minUnit，2：剂量单位doseUnit")
    private Integer minRetailUnit;


}
