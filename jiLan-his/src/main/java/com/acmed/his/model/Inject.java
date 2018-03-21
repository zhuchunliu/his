package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-03-21
 **/
@Data
@Table(name = "t_b_inject")
@NameStyle
public class Inject {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("单次剂量")
    private Double singleDose;

    @ApiModelProperty("频率 字典表：DrugFrequency")
    private Integer frequency;

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
