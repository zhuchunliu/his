package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AddMedicalRecordTplMo
 *
 * @author jimson
 * @date 2018/1/19
 */
@Data
public class AddMedicalRecordTplMo {
    @ApiModelProperty("id")
    private Integer id;
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

    @ApiModelProperty("模板名 王五")
    private String tplName;

    @ApiModelProperty("病例模板类型 字典表 MedicalRecordTpl")
    private String category;

    @ApiModelProperty("模板说明")
    private String declaratives;

    @ApiModelProperty("是否公开 0:否；1：是")
    private String isPublic;
}
