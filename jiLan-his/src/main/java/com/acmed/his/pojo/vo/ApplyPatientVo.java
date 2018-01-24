package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ApplyPatientVo
 *
 * @author jimson
 * @date 2018/1/24
 */
@Data
public class ApplyPatientVo {
    @ApiModelProperty("挂号单id")
    private String id;

    @ApiModelProperty("挂号单号 医疗机构id+排序    没付款的时候这个不存在")
    private String clinicNo;

    @ApiModelProperty("机构名")
    private String orgName;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("挂号费")
    private Double fee;

    @ApiModelProperty("状态 0:未就诊;1:已就诊,2:已取消")
    private String status;

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("总费用")
    private Double totalFee;

    @ApiModelProperty("医生名字")
    private String doctorName;

    @ApiModelProperty("预约时间")
    private String appointmentTime;

    @ApiModelProperty("是否是初诊   0不是初诊   1  是初诊")
    private Integer isFirst;
}
