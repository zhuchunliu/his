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

    @ApiModelProperty("药品分类名称")
    private String categoryName;

    @ApiModelProperty("处方类型")
    private String prescriptionTypeName;

    @ApiModelProperty("剂型名称")
    private String drugFormName;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("使用方法名称")
    private String useageName;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("单次剂量单位名称")
    private String singleDoseUnitName;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("一级零售价")
    private Double retailPrice;

    @ApiModelProperty("二级零售价")
    private Double minRetailPrice;

    @ApiModelProperty("二级零售价对应单位  1：小单位minUnitName，2：剂量单位doseUnitName")
    private Integer minPriceUnitType;

    @ApiModelProperty("库存数量")
    private String numName;

    @ApiModelProperty("是否有效 0:无；1：有")
    private Integer isValid;

    @ApiModelProperty("是否需要补充完善 0:不需要，1：需要")
    private Integer needSupplement;
}
