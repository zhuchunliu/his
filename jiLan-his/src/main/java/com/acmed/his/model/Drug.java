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
    private String drgCode;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("拼音")
    private String pinYin;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品；4：诊断试剂")
    private String category;

    @ApiModelProperty("药品类型 0:OTC药品; 1:处方药品")
    private String classification;

    @ApiModelProperty("包装单位")
    private String packUnit;

    @ApiModelProperty("计价单位")
    private String unit;

    @ApiModelProperty("包装数量")
    private Integer packNum;

    @ApiModelProperty("剂型")
    private String drugForm;

    @ApiModelProperty("生产厂家")
    private Integer manufacturer;

    @ApiModelProperty("供应商")
    private Integer supply;

    @ApiModelProperty("使用方法")
    private String useage;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("加成率")
    private Double markonpercent;

    @ApiModelProperty("药品备注")
    private String memo;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
