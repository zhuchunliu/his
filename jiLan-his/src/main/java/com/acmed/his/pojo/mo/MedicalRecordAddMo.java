package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * MedicalRecordAddMo
 *
 * @author jimson
 * @date 2018/1/24
 */
@Data
public class MedicalRecordAddMo {
    @ApiModelProperty("病例id   不填表示新增 填表示修改")
    private String id;

    @ApiModelProperty("挂号表id")
    private String applyId;

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

    @ApiModelProperty("备注")
    private String remark;
}
