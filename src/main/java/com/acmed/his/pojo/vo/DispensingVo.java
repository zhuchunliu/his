package com.acmed.his.pojo.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 发药列表
 *
 * Created by Darren on 2017-12-05
 **/
@Data
public class DispensingVo {

    @ApiModelProperty("挂号单id")
    private Integer id;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("门诊编号")
    private String clinicNo;

    @ApiModelProperty("费用")
    private Double totalFee;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("医生姓名")
    private String doctorName;

    @ApiModelProperty("就诊日期")
    private String createAt;

    @ApiModelProperty("诊断信息")
    private String diagnosis;
}
