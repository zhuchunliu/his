package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * ApplyDoctorVo
 *
 * @author jimson
 * @date 2017/11/30
 */
@Data
public class ApplyDoctorVo {
    @ApiModelProperty("挂号单id")
    private Integer id;

    @ApiModelProperty("挂号单号 医疗机构id+排序")
    private String clinicNo;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("机构名")
    private String orgName;

    @ApiModelProperty("患者id")
    private Integer patientId;

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

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;

    @ApiModelProperty("过期时间")
    private String expire;

    @ApiModelProperty("状态 0:未就诊;1:已就诊")
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
}
