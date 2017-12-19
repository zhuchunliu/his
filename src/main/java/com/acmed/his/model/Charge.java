package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 附加收费
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_charge")
@NameStyle
public class Charge {

    @Id
    @ApiModelProperty("处置id")
    private String id;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("挂号单号id")
    private String applyId;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("科室id")
    private Integer dept;

    @ApiModelProperty("费用类型")
    private String category;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;


}
