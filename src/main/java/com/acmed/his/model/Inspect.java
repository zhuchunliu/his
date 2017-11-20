package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * 检查单
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_inspect")
@NameStyle
public class Inspect {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("检查id")
    private Integer id;

    @ApiModelProperty("处方id")
    private Integer prescriptionId;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("患者id")
    private Integer patientId;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private String dept;

    @ApiModelProperty("检验检查类型")
    private String aim;

    @ApiModelProperty("检验检查类型")
    private String part;

    @ApiModelProperty("检验检查类型")
    private String category;

    @ApiModelProperty("病情摘要")
    private String summary;

    @ApiModelProperty("检查诊断")
    private String diagnosis;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
