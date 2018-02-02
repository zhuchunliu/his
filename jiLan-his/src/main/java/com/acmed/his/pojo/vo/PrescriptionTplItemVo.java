package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-23
 **/
@Data
public class PrescriptionTplItemVo {

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("药品名称")
    private String drugName;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品；4：诊断试剂")
    private String category;

    @ApiModelProperty("单价")
    private Double fee;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("单次剂量")
    private Integer dose;

    @ApiModelProperty("频率,用药频率 字典表：DrugFrequency")
    private String frequency;

    @ApiModelProperty("频率,用药频率名称")
    private String frequencyName;

    @ApiModelProperty("包装单位")
    private String packUnit;

    @ApiModelProperty("计价单位")
    private String unit;

    @ApiModelProperty("备注")
    private String memo;

}
