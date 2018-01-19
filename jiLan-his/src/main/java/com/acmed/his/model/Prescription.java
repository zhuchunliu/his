package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 处方
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_prescription")
@NameStyle
public class Prescription {
    @Id
    @ApiModelProperty("处方id")
    private String id;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("处方编号,医疗机构id+排序")
    private String prescriptionNo;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("处方类型 1:药，2：检查")
    private String category;

    @ApiModelProperty("总费用")
    private Double fee;

    @ApiModelProperty("付费状态 0:未付款；1：已经付款；2：部分退款；3：全额退款")
    private String isPaid;

    @ApiModelProperty("付费类型 字典表")
    private String feeType;

    @ApiModelProperty("是否发药 0:否；1:是")
    private String isDispensing;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;


}
