package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 病历
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_medicalrecord")
@NameStyle
public class MedicalRecord {

    @ApiModelProperty("挂号单号")
    @Id
    private Integer id;

    @ApiModelProperty("患者id")
    private Integer patientId;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private String dept;

    @ApiModelProperty("1 初诊、0 复诊")
    private String isFirst;

    @ApiModelProperty("发病日期")
    private Date onSetDate;

    @ApiModelProperty("主诉")
    private String chiefComplaint;

    @ApiModelProperty("神志：血压、血氧、血糖、瞳孔（左、右）、呼吸、脉搏")
    private String physicalExam;

    @ApiModelProperty("诊断信息")
    private String diagnosis;

    @ApiModelProperty("医嘱")
    private String advice;

    @ApiModelProperty("是否传染病 0：否；1：是")
    private String isContagious;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("修改时间")
    private Date modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;

    @ApiModelProperty("科室名字")
    private String deptName;
}
