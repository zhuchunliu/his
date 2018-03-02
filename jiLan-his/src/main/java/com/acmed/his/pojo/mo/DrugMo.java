package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-28
 **/
@Data
public class DrugMo {
    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("是否是国家基本药物  1 是 2 否")
    private Integer isEssential;

    @ApiModelProperty("药品分类 字典表：DrugClassification")
    private String category;

    @ApiModelProperty("处方类型 0：中药，1：西药")
    private Integer prescriptionType;

    @ApiModelProperty("剂型，字典表：DrugForm")
    private String drugForm;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("生产厂家")
    private String manufacturer;

    @ApiModelProperty("单位（g/条）大单位 字典表:Unit")
    private String unit;

    @ApiModelProperty("换算量")
    private Integer conversion;

    @ApiModelProperty("小单位 字典表： MinUnit")
    private String minUnit;

    @ApiModelProperty("剂量")
    private Double dose;

    @ApiModelProperty("剂量单位 字典表：DoseUnit")
    private String doseUnit;

    @ApiModelProperty("使用方法 字典表: Useage")
    private String useage;

    @ApiModelProperty("频率 字典表：DrugFrequency")
    private Integer frequency;

    @ApiModelProperty("单次用量")
    private Double singleDose;

    @ApiModelProperty("适应症状")
    private String symptoms;

    @ApiModelProperty("药品性状")
    private String character;

    @ApiModelProperty("不良反应")
    private String reactions;

    @ApiModelProperty("注意事项")
    private String matters;

    @ApiModelProperty("药品禁忌")
    private String taboo;

    @ApiModelProperty("条形码")
    private String barcode;

    @ApiModelProperty("原产地")
    private String origin;
}
