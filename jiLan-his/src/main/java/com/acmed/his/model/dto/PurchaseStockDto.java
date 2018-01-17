package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-17
 **/
@Data
public class PurchaseStockDto {

    @ApiModelProperty("入库批号")
    private String batchNumber;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品；4：诊断试剂")
    private String category;

    @ApiModelProperty("生产厂家")
    private String manufacturerName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("采购数量")
    private Double num;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("采购批发总额")
    private Double bidFee;

    @ApiModelProperty("采购处方总额")
    private Double retailFee;
}
