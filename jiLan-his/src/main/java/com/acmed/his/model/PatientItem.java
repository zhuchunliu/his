package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * PatientItem
 *
 * @author jimson
 * @date 2018/1/16
 */
@Data
@Table(name = "t_b_patient_item")
@NameStyle
public class PatientItem {
    @Id
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("患者id")
    private String patientId;

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

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("是否是黑名单  0不是黑名单   1 是黑名单")
    private Integer blackFlag;

    @ApiModelProperty("机构id")
    private Integer orgCode;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("身份证号码")
    private String idCard;

    @ApiModelProperty("年龄")
    private Integer age;
}
