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

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("挂号表id")
    private String applyId;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("患者庫id")
    private String patientItemId;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("1 初诊、0 复诊")
    private String isFirst;

    @ApiModelProperty("发病日期")
    private String onSetDate;

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
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;

    @ApiModelProperty("科室id")
    private Integer dept;

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("备注")
    private String remark;


}
