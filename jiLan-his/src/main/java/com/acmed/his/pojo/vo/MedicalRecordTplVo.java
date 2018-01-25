package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;

/**
 * MedicalRecordTplVo
 *
 * @author jimson
 * @date 2018/1/25
 */
@Data
public class MedicalRecordTplVo {
    @ApiModelProperty("id")
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

    @ApiModelProperty("病例模板类型 名")
    private String categoryName;

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

    @ApiModelProperty("是否是自己的 自己的才可以 修改 0 不是自己的   1 是自己的 ")
    private Integer isSelf;

    @ApiModelProperty("是否有效(启用) 0:无；1：有")
    private String isValid;

    @ApiModelProperty("是否公开 0:否；1：是")
    private String isPublic;
}
