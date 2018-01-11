package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 处方详情
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_prescription_item")
@NameStyle
public class PrescriptionItem {

    @Id
    @ApiModelProperty("用药id")
    private String id;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("药品类型")
    private String category;

    @ApiModelProperty("用药名称")
    private String drugName;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("途径")
    private String way;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("频率")
    private Integer frequency;

    @ApiModelProperty("单次剂量")
    private Integer dose;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
    private Integer payStatus;

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("组号")
    private String groupNum;
}
