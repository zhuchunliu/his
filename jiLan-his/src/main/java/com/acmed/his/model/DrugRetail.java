package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 药品零售
 *
 * Created by Darren on 2018-04-13
 **/
@Data
@Table(name = "t_b_drug_retail")
@NameStyle
public class DrugRetail {

    @Id
    @ApiModelProperty("零售id")
    private String id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("患者id")
    private String patientItemId;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("总费用")
    private Double fee;

    @ApiModelProperty("支付状态 0:未支付,1:已支付")
    private Integer payStatus;

    @ApiModelProperty("付费类型 字典表")
    private String feeType;

    @ApiModelProperty("是否删除 0:无；1：有")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
