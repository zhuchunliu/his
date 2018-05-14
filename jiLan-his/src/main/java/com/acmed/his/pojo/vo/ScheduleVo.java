package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-21
 **/
@Data
public class ScheduleVo {

    @ApiModelProperty("用户主键表")
    private Integer userid;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("头像")
    private String avatar;

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
