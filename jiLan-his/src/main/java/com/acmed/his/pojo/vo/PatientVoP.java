package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PatientVoP
 * 患者信息
 * @author jimson
 * @date 2018/3/5
 */
@Data
public class PatientVoP {
    @ApiModelProperty("登录名")
    private String userName;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("出生日期")
    private String dateOfBirth;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("号码")
    private String mobile;

    @ApiModelProperty("职业")
    private String prof;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("社保卡")
    private String socialCard;

    @ApiModelProperty("拼音")
    private String inputCode;

    @ApiModelProperty("头像")
    private String avatar;
}
