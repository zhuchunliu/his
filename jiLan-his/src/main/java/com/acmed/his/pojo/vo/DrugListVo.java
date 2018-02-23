package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * Created by Darren on 2018-02-23
 **/
@Data
public class DrugListVo {

    @Id
    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("是否是国家基本药物  1 是 2 否")
    private Integer isEssential;

    @ApiModelProperty("药品分类 字典表：DrugClassification")
    private String category;

    @ApiModelProperty("药品分类名称")
    private String categoryName;

    @ApiModelProperty("处方类型 0：中药，1：西药")
    private Integer prescriptionType;

    @ApiModelProperty("剂型，字典表：DrugForm")
    private String drugForm;

    @ApiModelProperty("剂型名称")
    private String drugFormName;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品名称拼音")
    private String pinYin;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品名称拼音")
    private String goodsPinYin;

    @ApiModelProperty("生产厂家")
    private Integer manufacturer;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("单位（g/条）大单位 字典表:Unit")
    private String unit;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("换算量")
    private Integer conversion;

    @ApiModelProperty("小单位 字典表： MinUnit")
    private String minUnit;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty("剂量")
    private Double dose;

    @ApiModelProperty("剂量单位 字典表：DoseUnit")
    private String doseUnit;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("使用方法 字典表: Useage")
    private String useage;

    @ApiModelProperty("使用方法名称")
    private String useageName;

    @ApiModelProperty("频率 字典表：DrugFrequency")
    private Integer frequency;

    @ApiModelProperty("频率名称")
    private String frequencyName;

    @ApiModelProperty("单次用量")
    private Double singleDose;



    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("库存数量")
    private Double num;
}
