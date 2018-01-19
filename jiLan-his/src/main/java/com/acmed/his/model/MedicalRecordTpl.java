package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 病历模板
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_medicalrecord_tpl")
@NameStyle
public class MedicalRecordTpl {
    @ApiModelProperty("id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("科室id")
    private Integer dept;

    @ApiModelProperty("医生id")
    private Integer userId;

    @ApiModelProperty("主诉")
    private String chiefComplaint;

    @ApiModelProperty("神志：血压、血氧、血糖、瞳孔（左、右）、呼吸、脉搏")
    private String physicalExam;

    @ApiModelProperty("诊断信息")
    private String diagnosis;

    @ApiModelProperty("医嘱")
    private String advice;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("删除标记  0未删除  1已删除")
    private String removed;

    @ApiModelProperty("模板名 王五")
    private String tplName;

    @ApiModelProperty("模板名拼音    WW")
    private String tplNamePinYin;

    @ApiModelProperty("病例模板类型 字典表 MedicalRecordTpl")
    private String category;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;

    @ApiModelProperty("模板说明")
    private String declaratives;
}
