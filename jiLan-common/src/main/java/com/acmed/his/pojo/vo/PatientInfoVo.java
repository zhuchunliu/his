package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PatientInfoVo
 *
 * @author jimson
 * @date 2017/11/22
 */
@Data
public class PatientInfoVo {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("登录名")
    private String userName;

    @ApiModelProperty("患者真实姓名")
    private String realName;

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
}
