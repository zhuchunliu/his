package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-05-04
 **/
@Data
public class DrugImportMo {

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("药品分类名称")
    private String categoryName;

    @ApiModelProperty("剂型名称")
    private String drugFormName;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("条形码")
    private String barcode;

    @ApiModelProperty("准字号")
    private String approvalNumber;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("换算量")
    private Integer conversion;

    @ApiModelProperty("剂量")
    private Double dose;

    @ApiModelProperty("使用方法名称")
    private String useageName;

    @ApiModelProperty("频率名称")
    private String frequencyName;

    @ApiModelProperty("单次用量")
    private Double singleDose;

    @ApiModelProperty("药品规格")
    private String spec;

}
