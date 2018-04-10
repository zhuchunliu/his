package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-14
 **/
@Data
public class DrugWarnDto{

    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("生产厂家 对应生产商")
    private Integer manufacturer;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("单位（g/条）大单位 字典表:Unit")
    private Integer unit;

    @ApiModelProperty("小单位 字典表： Unit")
    private Integer minUnit;

    @ApiModelProperty("剂量单位 字典表：Unit")
    private Integer doseUnit;

    @ApiModelProperty("大单位数量")
    private Integer num;

    @ApiModelProperty("小单位数量")
    private Integer minNum;

    @ApiModelProperty("剂量单位库存数量")
    private Double doseNum;

    @ApiModelProperty("安全库存数量")
    private Integer safetyNum;

    @ApiModelProperty("预警类型 1：库存不足，2：过期提醒 , 3:库存不足+过期提醒")
    private Integer warnType;

    @ApiModelProperty("过期数量")
    private Integer expireNum;

    @ApiModelProperty("过期小单位数量")
    private Integer expireMinNum;

    @ApiModelProperty("过期剂量")
    private Double expireDoseNum;
}
