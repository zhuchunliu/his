package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-04-13
 **/
@Data
public class DrugRetailDto {
    @ApiModelProperty("零售id")
    private String id;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("挂号费")
    private Double fee;

    @ApiModelProperty("支付状态 0:未支付,1:已支付")
    private Integer payStatus;

    @ApiModelProperty("是否删除 0:无；1：有")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;
}
