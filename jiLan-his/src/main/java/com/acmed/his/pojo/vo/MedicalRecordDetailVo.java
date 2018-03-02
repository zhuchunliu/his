package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * MedicalRecordDetailVo
 *
 * @author jimson
 * @date 2018/1/25
 */
@Data
public class MedicalRecordDetailVo {
    @ApiModelProperty("病例id")
    private String id;

    @ApiModelProperty("挂号表id")
    private String applyId;

    @ApiModelProperty("患者庫id")
    private String patientItemId;

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

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("处方列表")
    private List<PrescriptionVo> prescriptionVoList;

    @ApiModelProperty("医生名字")
    private String doctorName;
}
