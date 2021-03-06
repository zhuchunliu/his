package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 收货发药
 * Created by Darren on 2017-12-22
 **/
@Data
public class DispensingDto {

    @ApiModelProperty("挂号单id")
    private String applyId;

    @ApiModelProperty("门诊编号")
    private String clinicNo;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("号码")
    private String mobile;

    @ApiModelProperty("医生姓名")
    private String doctorName;

    @ApiModelProperty("就诊日期")
    private String diagnoseDate;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("是否发药 0:否；1:是")
    private String isDispensing;

    @ApiModelProperty("是否已付费 0:未付款；1：已经付款；2：部分退款；3：全额退款")
    private String isPaid;

    @ApiModelProperty("是否包含药品 1:是，0:否")
    private String contanisMedicine;

    @ApiModelProperty("是否包含云药房药品 1:是，0:否")
    private String contanisZyMedicine;
}
