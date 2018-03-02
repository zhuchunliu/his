package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 药品库
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_drug")
@NameStyle
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ApiModelProperty("药品名称拼音")
    private String pinYin;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品名称拼音")
    private String goodsPinYin;

    @ApiModelProperty("生产厂家 对应生产商")
    private String manufacturer;

    @ApiModelProperty("生产厂家 对应生产商")
    private Integer manufacturerId;

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

    @ApiModelProperty("药品规格")
    private String spec;

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



    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("库存数量")
    private Double num;

    @ApiModelProperty("供应商")
    private Integer supply;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;



    @ApiModelProperty("包装数量")
    private Integer packNum;

    @ApiModelProperty("药品类型 0:OTC药品; 1:处方药品")
    private String classification;

    @ApiModelProperty("包装单位")
    private String packUnit;

    @ApiModelProperty("加成率")
    private Double markonpercent;

    @ApiModelProperty("药品备注")
    private String memo;

}
