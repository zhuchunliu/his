package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-04-10
 **/
@Data
public class ZYDrugListObj {

    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("药店电话")
    private String tel;

    @ApiModelProperty("药店名称")
    private String storeName;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("是否为OTC药品 0为非otc药品 1为otc药品")
    private String OTC;

    @ApiModelProperty("药品名")
    private String cnName;

    @ApiModelProperty("药品规格")
    private String form;

    @ApiModelProperty("生产厂家")
    private String companyName;

    @ApiModelProperty("药品功效")
    private String indication;

    @ApiModelProperty("国药准字号")
    private String number;

    @ApiModelProperty("库存数")
    private String storage;

    @ApiModelProperty("药品图片")
    private String picPath;

    @ApiModelProperty("药品价格")
    private String goodsPrice;

    @ApiModelProperty("服务费")
    private String servicePrice;

    @ApiModelProperty("距离")
    private String distance;

    @ApiModelProperty("是否展示服务费 0不展示 1为展示")
    private String showService;

    @ApiModelProperty("所在城市")
    private String city;

}
