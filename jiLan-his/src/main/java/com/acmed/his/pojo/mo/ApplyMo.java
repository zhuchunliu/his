package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-07
 **/
@Data
public class ApplyMo {

    @ApiModelProperty("患者详情主键")
    private String patientItemId;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("患者端必须是18位身份证    医生端可以是8位生日")
    private String idcard;

    @ApiModelProperty("社保卡")
    private String socialCard;

    @ApiModelProperty("出生日期")
    private String dateOfBirth;

    @ApiModelProperty("医生id 必填")
    private Integer doctorId;

    @ApiModelProperty("预约时间")
    private String appointmentTime;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("过敏史")
    private String anaphylaxis;

    @ApiModelProperty("关系")
    private String relation;

    @ApiModelProperty("就诊人id 和患者基础信息选填一个 优先就诊人id")
    private String patientCardId;
}
