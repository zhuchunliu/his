package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 排班表
 *
 * Created by Darren on 2017-12-20
 **/
@Data
@Table(name = "t_b_schedule")
@NameStyle
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

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

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;
}
