package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-05
 **/
@Data
public class ZyDispenseOrderDto {
    @ApiModelProperty("性别")
    private Integer gender;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("订单主键")
    private String applyId;

    @ApiModelProperty("提交时间")
    private String submitTime;

    @ApiModelProperty("状态 0:已完成，1:待收货,2：已发放")
    private Integer status;

}
