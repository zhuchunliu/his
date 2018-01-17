package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Darren on 2017-12-28
 **/
@Data
public class DrugVo {

    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

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

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("使用方法")
    private String useage;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("库存数量")
    private Double num;

    @ApiModelProperty("加成率")
    private Double markonpercent;

    @ApiModelProperty("药品备注")
    private String memo;

}
