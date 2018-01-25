package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * UserPatientVo
 *
 * @author jimson
 * @date 2018/1/25
 */
@Data
public class UserPatientVo {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("科室名称")
    private String deptName;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("挂号费")
    private Double applyfee;

    @ApiModelProperty("门诊级别")
    private String diagnosLevel;

    @ApiModelProperty("门诊级别 名")
    private String diagnosLevelStr;

    @ApiModelProperty("医生职称")
    private String duty;

    @ApiModelProperty("医生职称 名")
    private String dutyStr;

    @ApiModelProperty("擅长")
    private String expertin;

    @ApiModelProperty("简介")
    private String introduction;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("年龄")
    private Integer age;
}
