package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-21
 **/
@Data
public class ScheduleDto {
    @ApiModelProperty("用户主键表")
    private Integer userid;

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

    @ApiModelProperty("是否自动循环 0:否；1:是")
    private String circle;
}
