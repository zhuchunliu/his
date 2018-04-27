package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by Darren on 2018-01-23
 **/
@Data
public class PrescriptionTplItemVo {

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("药品名称")
    private String drugName;

    @ApiModelProperty("药品分类 字典表：DrugClassification")
    private Integer category;

    @ApiModelProperty("药品分类名称")
    private String categoryName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("单位数量")
    private Integer num;

    @ApiModelProperty("小单位数量")
    private Integer minNum;

    @ApiModelProperty("剂量单位库存数量")
    private Double doseNum;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("频率,用药频率 字典表：DrugFrequency")
    private Integer frequency;

    @ApiModelProperty("频率,用药频率名称")
    private String frequencyName;

    @ApiModelProperty("一级单位名称")
    private String unitName;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty("二级零售价对应单位  1：小单位minUnit，2：剂量单位doseUnit")
    private Integer minPriceUnitType;

    @ApiModelProperty("二级单位名称 药品 minPriceUnitType：1代表minUnit,2代表doseUnit")
    private String minOrDoseUnitName;

    @ApiModelProperty("单次剂量")
    private Double singleDose;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("单位类型 1：一级计价单位，2：二级计价单位")
    private Integer unitType;

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("一级单位零售价")
    private Double retailPrice;

    @ApiModelProperty("二级单位零售价")
    private Double minRetailPrice;



}
