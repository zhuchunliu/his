package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class UserMo implements Serializable{
    @ApiModelProperty("id null：新增，not null ：编辑")
    private Integer id;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("所属机构 默认：当前操作人所属机构")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("用户类型 字典表：UserCategory")
    private String category;

    @ApiModelProperty("状态：0启用,1禁用")
    private String status;

    @ApiModelProperty("挂号费")
    private Double applyfee;

    @ApiModelProperty("门诊级别 字典表:DiagnosisLevel")
    private String diagnosLevel;

    @ApiModelProperty("医生职称 字典表：Duty")
    private String duty;

    @ApiModelProperty("擅长")
    private String expertin;

    @ApiModelProperty("简介")
    private String introduction;

    @ApiModelProperty("头像")
    private String avatar;



}
