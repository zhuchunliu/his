package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-04
 **/
@Data
public class DrugStockPrice {

    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("处方价,大单位")
    private Double retailPrice;

    @ApiModelProperty("小单位零售价")
    private Double minRetailPrice;

    @ApiModelProperty("小单位零售价对应单位  1：小单位minUnit，2：剂量单位doseUnit")
    private Integer minRetailUnit;
}
