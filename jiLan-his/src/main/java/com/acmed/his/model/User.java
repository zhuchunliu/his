package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * User
 *
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_user")
@NameStyle
public class User implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("昵称")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String passWd;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("出生日期")
    private String dateOfBirth;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("科室名称")
    private String deptName;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("用户类型")
    private String category;

    @ApiModelProperty("状态 0:禁用，1：启用")
    private String status;

    @ApiModelProperty("挂号费")
    private Double applyfee;

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

    @ApiModelProperty("最后登录时间")
    private String lastLogin;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("微信openid")
    private String openid;

    @ApiModelProperty("微信绑定的unionid")
    private String unionid;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;

    @ApiModelProperty("注册时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("限号数量")
    private Integer applyNum;

    @ApiModelProperty("分享码")
    private String shareCode;

}
