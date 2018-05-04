package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * Created by Darren on 2018-03-22
 **/
@Data
public class DrugDictDetailVo {
    @ApiModelProperty("药品字典id")
    private String id;

    @ApiModelProperty("是否是国家基本药物  1 是 2 否")
    private Integer isEssential;

    @ApiModelProperty("药品分类 字典表：DrugClassification")
    private Integer category;

    @ApiModelProperty("药品分类名称")
    private String categoryName;

    @ApiModelProperty("处方类型 字典表：PrescriptionType")
    private Integer prescriptionType;

    @ApiModelProperty("处方类型")
    private String prescriptionTypeName;

    @ApiModelProperty("剂型，字典表：DrugForm")
    private Integer drugForm;

    @ApiModelProperty("剂型名称")
    private String drugFormName;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("生产厂家")
    private Integer manufacturer;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("单位（g/条）大单位 字典表:Unit")
    private Integer unit;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("换算量")
    private Integer conversion;

    @ApiModelProperty("小单位 字典表： Unit")
    private Integer minUnit;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty("剂量")
    private Double dose;

    @ApiModelProperty("剂量单位 字典表：Unit")
    private Integer doseUnit;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("使用方法 字典表: Useage")
    private Integer useage;

    @ApiModelProperty("使用方法名称")
    private String useageName;

    @ApiModelProperty("频率 字典表：DrugFrequency")
    private Integer frequency;

    @ApiModelProperty("频率名称")
    private String frequencyName;

    @ApiModelProperty("单次用量")
    private Double singleDose;

    @ApiModelProperty("适应症状")
    private String symptoms;

    @ApiModelProperty("药品性状")
    private String characters;

    @ApiModelProperty("不良反应")
    private String reactions;

    @ApiModelProperty("注意事项")
    private String matters;

    @ApiModelProperty("药品禁忌")
    private String taboo;

    @ApiModelProperty("药品说明")
    private String instructions;

    @ApiModelProperty("条形码")
    private String barcode;

    @ApiModelProperty("原产地")
    private String origin;


}
