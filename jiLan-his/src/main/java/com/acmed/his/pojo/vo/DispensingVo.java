package com.acmed.his.pojo.vo;

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
    private String applyId;

    @ApiModelProperty("门诊编号")
    private String clinicNo;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别 0:男;1:女")
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

    @ApiModelProperty("状态 1:未收费、2:已收费[已付款未发药处方、已付款的检查处方]、3:已退款、4：已完成")
    private String status;

    @ApiModelProperty("是否包含药品 1:是，0:否")
    private String contanisMedicine;

}
