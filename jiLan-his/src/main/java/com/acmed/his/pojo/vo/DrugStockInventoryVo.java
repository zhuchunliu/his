package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-13
 **/
@Data
public class DrugStockInventoryVo {

    @ApiModelProperty("库存详情id")
    private Integer stockId;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("大单位库存数量")
    private Integer num;

    @ApiModelProperty("小单位库存数量")
    private Integer minNum;

    @ApiModelProperty("剂量单位库存数量")
    private Double doseNum;
}
