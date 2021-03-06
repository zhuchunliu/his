package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-01-05
 **/
@Data
public class ScheduleApplyVo {
    @ApiModelProperty("周几")
    private String week;

    @ApiModelProperty("日期")
    private String date;

    private List<ScheduleApplyDetail> detailList;

    @Data
    public class ScheduleApplyDetail{
        @ApiModelProperty("用户主键表")
        private Integer userid;

        @ApiModelProperty("用户名")
        private String userName;

        @ApiModelProperty("头像")
        private String avatar;

        @ApiModelProperty("医生职称")
        private String dutyName;

        @ApiModelProperty("性别")
        private String gender;

        @ApiModelProperty(value = "出生年月",hidden = true)
        private String dateOfBirth;

        @ApiModelProperty("年龄")
        private Integer age;


        @ApiModelProperty("日期")
        private String date;

        @ApiModelProperty("排班")
        private String schedule;

        @ApiModelProperty("门诊级别")
        private String diagnosLevelName;

        @ApiModelProperty("科室名称")
        private String deptName;

        @ApiModelProperty("挂号费")
        private Double applyFee;

        @ApiModelProperty("擅长")
        private String expertin;

        @ApiModelProperty("简介")
        private String introduction;

        @ApiModelProperty("限号数量")
        private Integer applyNum;

        @ApiModelProperty("已经挂号数量")
        private Integer occupyNum;

        @ApiModelProperty("挂号日期")
        private String appointmentTime;


    }
}
