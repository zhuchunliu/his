package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 患者信息
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_patient")
@NameStyle
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("出生日期")
    private String dateOfBirth;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("号码")
    private String mobile;

    @ApiModelProperty("体重")
    private Double weight;

    @ApiModelProperty("职业")
    private String prof;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("社保卡")
    private String socialCard;

    @ApiModelProperty("拼音")
    private String inputCode;

    @ApiModelProperty("微信openid")
    private String openid;

    @ApiModelProperty("微信unionid")
    private String unionid;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("修改时间")
    private Date modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
