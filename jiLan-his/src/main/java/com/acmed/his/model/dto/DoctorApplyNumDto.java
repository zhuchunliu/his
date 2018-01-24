package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DoctorApplyNumDto
 *
 * @author jimson
 * @date 2018/1/10
 */
@Data
public class DoctorApplyNumDto {
    @ApiModelProperty("头像")
    private String avatar;
    @ApiModelProperty("门诊级别")
    private String diagnosLevel;
    @ApiModelProperty("医生职称")
    private String duty;
    @ApiModelProperty("医生名")
    private String userName;
    @ApiModelProperty("预约量")
    private Integer applyNum;
    @ApiModelProperty("科室名称")
    private String deptName;
}
