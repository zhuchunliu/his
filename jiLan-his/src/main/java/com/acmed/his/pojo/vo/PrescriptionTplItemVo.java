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

    @ApiModelProperty("单价")
    private Double fee;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("单次剂量")
    private Integer dose;

    @ApiModelProperty("频率,用药频率")
    private Float frequency;

    @ApiModelProperty("包装单位")
    private String packUnit;

    @ApiModelProperty("计价单位")
    private String unit;

    @ApiModelProperty("备注")
    private String memo;

}
