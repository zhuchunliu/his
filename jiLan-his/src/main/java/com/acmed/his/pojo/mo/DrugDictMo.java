package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-22
 **/
@Data
public class DrugDictMo {
    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("是否是国家基本药物  1 是 2 否")
    private Integer isEssential;

    @ApiModelProperty("药品分类 字典表：DrugClassification")
    private Integer category;

    @ApiModelProperty("处方类型 字典表：PrescriptionType")
    private Integer prescriptionType;

    @ApiModelProperty("剂型，字典表：DrugForm")
    private Integer drugForm;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品名称拼音")
    private String pinYin;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品名称拼音")
    private String goodsPinYin;

    @ApiModelProperty("生产厂家 对应生产商")
    private Integer manufacturer;

    @ApiModelProperty("单位（g/条）大单位 字典表:Unit")
    private Integer unit;

    @ApiModelProperty("换算量")
    private Integer conversion;

    @ApiModelProperty("小单位 字典表： Unit")
    private Integer minUnit;

    @ApiModelProperty("安全库存数量")
    private Integer safetyNum;

    @ApiModelProperty("剂量")
    private Double dose;

    @ApiModelProperty("剂量单位 字典表：Unit")
    private Integer doseUnit;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("使用方法 字典表: Useage")
    private Integer useage;

    @ApiModelProperty("频率 字典表：DrugFrequency")
    private Integer frequency;

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
