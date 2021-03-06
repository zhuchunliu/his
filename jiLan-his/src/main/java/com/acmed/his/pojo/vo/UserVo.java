package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2017-11-26
 **/
@Data
public class UserVo {
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

    @ApiModelProperty("出生日期")
    private String dateOfBirth;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("所属机构")
    private Integer orgName;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("科室名称")
    private String deptName;

    @ApiModelProperty("用户类型 字典表：UserCategory")
    private String category;

    @ApiModelProperty("用户类型名称")
    private String categoryName;

    @ApiModelProperty("状态：0启用,1禁用")
    private String status;

    @ApiModelProperty("挂号费")
    private Double applyfee;

    @ApiModelProperty("门诊级别 字典表:DiagnosisLevel")
    private String diagnosLevel;

    @ApiModelProperty("门诊级别名")
    private String diagnosLevelName;

    @ApiModelProperty("医生职称 字典表：Duty")
    private String duty;

    @ApiModelProperty("医生职称名")
    private String dutyName;

    @ApiModelProperty("擅长")
    private String expertin;

    @ApiModelProperty("简介")
    private String introduction;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("绑定微信  0绑定  1未绑定")
    private Integer bindWx;

    private List<UserRoleVo> userRoleVoList;

    @Data
    public class UserRoleVo{
        @ApiModelProperty("角色主键")
        private Integer roleId;

        @ApiModelProperty("权限组名称")
        private String roleName;

        @ApiModelProperty("是否选中 0:未选中，1：已结选中")
        private String isChecked;

    }

}
