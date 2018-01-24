package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * ApplyDoctorVo
 *
 * @author jimson
 * @date 2017/11/30
 */
@Data
public class ApplyDoctorDto {
    @ApiModelProperty("挂号单id")
    private String id;

    @ApiModelProperty("挂号单号 医疗机构id+排序")
    private String clinicNo;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("机构名")
    private String orgName;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("拼音")
    private String pinYin;

    @ApiModelProperty("挂号费")
    private Double fee;

    @ApiModelProperty("是否已付费 0:否; 1:是")
    private String isPaid;

    @ApiModelProperty("付费类型")
    private String feeType;

    @ApiModelProperty("过期时间")
    private String expire;

    @ApiModelProperty("状态 0:未就诊;1:已就诊,2:已取消")
    private String status;

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("科室id")
    private Integer dept;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("总费用")
    private Double totalFee;

    @ApiModelProperty("号码")
    private String mobile;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("预约时间")
    private String appointmentTime;

    @ApiModelProperty("是否是初诊   0不是初诊   1  是初诊")
    private Integer isFirst;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("过敏史")
    private String anaphylaxis;

    @ApiModelProperty("个人史")
    private String personalHistory;

    @ApiModelProperty("家族史")
    private String familyHistory;
}
