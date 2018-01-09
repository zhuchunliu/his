package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-07
 **/
@Data
public class ApplyMo {
    @ApiModelProperty("科室Id")
    private Integer dept;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("身份证")
    private String idcard;

    @ApiModelProperty("社保卡")
    private String socialCard;

    @ApiModelProperty("医生id")
    private Integer doctorId;

    @ApiModelProperty("预约时间")
    private String appointmentTime;
}
