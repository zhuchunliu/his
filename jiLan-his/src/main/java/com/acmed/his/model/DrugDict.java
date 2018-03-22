package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 药品字典
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_drugdict")
@NameStyle
public class DrugDict {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品名称拼音")
    private String pinYin;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品名称拼音")
    private String goodsPinYin;

    @ApiModelProperty("药品分类名称 ")
    private String categoryName;

    @ApiModelProperty("药品分类 字典表：DrugClassification")
    private Integer category;

    @ApiModelProperty("剂型")
    private String drugFormName;

    @ApiModelProperty("剂型 字典表：DrugForm")
    private Integer drugForm;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("生产厂家 对应生产商")
    private Integer manufacturer;

    @ApiModelProperty("准字号")
    private String approvalNumber;

    @ApiModelProperty("条形码")
    private String barcode;

    @ApiModelProperty("单位（g/条）大单位 ")
    private String unitName;

    @ApiModelProperty("单位主键 字典表:Unit")
    private Integer unit;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("药品说明")
    private String instructions;

    @ApiModelProperty("外部id")
    private String outId;



    @ApiModelProperty("是否是国家基本药物  1 是 2 否")
    private Integer isEssential;

    @ApiModelProperty("处方类型 字段表：PrescriptionType")
    private Integer prescriptionType;

    @ApiModelProperty("换算量")
    private Integer conversion;

    @ApiModelProperty("小单位 字典表： Unit")
    private Integer minUnit;

    @ApiModelProperty("剂量")
    private Double dose;

    @ApiModelProperty("剂量单位 字典表：Unit")
    private Integer doseUnit;

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

    @ApiModelProperty("原产地")
    private String origin;

    @ApiModelProperty("是否删除 0:无；1：有")
    private String removed;

}
