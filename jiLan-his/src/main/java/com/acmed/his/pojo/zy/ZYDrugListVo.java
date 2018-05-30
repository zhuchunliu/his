package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-04-10
 **/
@Data
public class ZYDrugListVo {

    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("药店名称")
    private String storeName;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("药品名")
    private String drugName;

    @ApiModelProperty("生产厂家")
    private String manufacturerName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("药品价格")
    private String retailPrice;

    @ApiModelProperty("所在城市")
    private String city;

    @ApiModelProperty("使用库存数")
    private Integer useNum;

    @ApiModelProperty("距离")
    private String distance;


}
