package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * MedicalReDto
 *
 * @author jimson
 * @date 2017/12/1
 */
@Data
public class MedicalReDto {
    /**
     * 科室id
     */
    @ApiModelProperty("科室id")
    private Integer deptId;
    /**
     * 病历id
     */
    @ApiModelProperty("病历id")
    private Integer medicalRecordId;
    /**
     * 机构id
     */
    @ApiModelProperty("机构id")
    private Integer orgCode;
    /**
     * 挂号单id
     */
    @ApiModelProperty("挂号单id")
    private Integer applyId;
    /**
     * 患者id
     */
    @ApiModelProperty("患者id")
    private Integer patientId;
    /**
     * 科室名
     */
    @ApiModelProperty("科室名")
    private String deptName;
    /**
     * 机构名
     */
    @ApiModelProperty("机构名")
    private String orgName;
    /**
     * 医生名
     */
    @ApiModelProperty("医生名")
    private String doctorName;
    /**
     * 医生id
     */
    @ApiModelProperty("医生id")
    private Integer doctorId;
    /**
     * 创建时间
     */
    @ApiModelProperty("创建时间")
    private String createAt;
}
