package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-07
 **/
@Data
public class DrugStockVo {

    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty(value = "计价单位")
    private Integer unit;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty(value = "小单位 字典表： MinUnit")
    private Integer minUnit;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty(value = "剂量单位 字典表：DoseUnit")
    private Integer doseUnit;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("生产厂家")
    private String manufacturerName;

    @ApiModelProperty("库存数量")
    private String numName;

    @ApiModelProperty("批发价")
    private String bid;

    @ApiModelProperty("一级处方价")
    private Double retailPrice;

    @ApiModelProperty("一级处方价名称")
    private String retailPriceName;

    @ApiModelProperty("二级处方价")
    private Double minRetailPrice;

    @ApiModelProperty("二级处方价名称")
    private String minRetailPriceName;

    @ApiModelProperty("二级处方价对应单位  1：小单位minUnit，2：剂量单位doseUnit")
    private Integer minPriceUnitType;

}
