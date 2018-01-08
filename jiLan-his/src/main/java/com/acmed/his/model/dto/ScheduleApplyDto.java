package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-08
 **/
@Data
public class ScheduleApplyDto {

    @ApiModelProperty("用户主键表")
    private Integer userid;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("挂号费")
    private Double applyFee;

    @ApiModelProperty("门诊级别")
    private String diagnosLevel;

    @ApiModelProperty("医生职称")
    private String duty;

    @ApiModelProperty("擅长")
    private String expertin;

    @ApiModelProperty("简介")
    private String introduction;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    @ApiModelProperty("周一排班情况")
    private String monday;

    @ApiModelProperty("周二排班情况")
    private String tuesday;

    @ApiModelProperty("周三排班情况")
    private String wednesday;

    @ApiModelProperty("周四排班情况")
    private String thursday;

    @ApiModelProperty("周五排班情况")
    private String friday;

    @ApiModelProperty("周六排班情况")
    private String saturday;

    @ApiModelProperty("周日排班情况")
    private String sunday;
}
