package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PatientItemUpMo
 *
 * @author jimson
 * @date 2018/1/23
 */
@Data
public class PatientItemUpMo {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("身高")
    private BigDecimal height;

    @ApiModelProperty("体重")
    private BigDecimal weight;

    @ApiModelProperty("血型 A  B   AB")
    private String blood;

    @ApiModelProperty("血型 RH阴性")
    private String bloodType;

    @ApiModelProperty("左耳听力")
    private String leftEar;

    @ApiModelProperty("右耳听力")
    private String rightEar;

    @ApiModelProperty("左眼视力")
    private String leftEye;

    @ApiModelProperty("右眼视力")
    private String rightEye;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("紧急联系人")
    private String emergencyContact;

    @ApiModelProperty("紧急联系人号码")
    private String emergencyMobile;

    @ApiModelProperty("关系")
    private String relation;

    @ApiModelProperty("过敏史")
    private String anaphylaxis;

    @ApiModelProperty("个人史")
    private String personalHistory;

    @ApiModelProperty("家族史")
    private String familyHistory;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("身份证号码")
    private String idCard;

    @ApiModelProperty("社保卡")
    private String socialCard;

}
