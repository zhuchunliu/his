package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

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
    @ApiModelProperty("检查id")
    private String id;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private String dept;

    @ApiModelProperty("检查目的")
    private String aim;

    @ApiModelProperty("检查部位")
    private String part;

    @ApiModelProperty("检查类型")
    private String category;

    @ApiModelProperty("病情摘要")
    private String summary;

    @ApiModelProperty("检查诊断")
    private String diagnosis;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
    private Integer payStatus;

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("要求")
    private String requirement;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("序号")
    private Integer sn;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
