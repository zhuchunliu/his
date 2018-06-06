package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-04
 **/
@Data
public class ZyOrderItemReceiveDto {
    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("快递单号")
    private String expressNo;

    @ApiModelProperty("提交时间")
    private String submitTime;

    @ApiModelProperty("收货状态 -1：不可收货，无物流信息, 0:待收货；1:已经收货；2:缺货")
    private Integer recepitStatus;

    @ApiModelProperty("详情id")
    private String itemId;

    @ApiModelProperty("处方编号,医疗机构id+排序")
    private String prescriptionNo;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("用药名称")
    private String drugName;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("收货数量")
    private Integer receiveNum;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("药品厂家 - 掌药")
    private String manufacturerName;
}
