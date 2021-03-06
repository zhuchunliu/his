package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-05-23
 **/
@Data
public class ZyOrderItemUnsubmitDto {

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("药品费用")
    private Double drugFee;

    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("详情id")
    private String itemId;

    @ApiModelProperty("挂号主键")
    private String applyId;

    @ApiModelProperty("处方编号,医疗机构id+排序")
    private String prescriptionNo;

    @ApiModelProperty("用药名称")
    private String drugName;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("药品厂家 - 掌药")
    private String manufacturerName;
}
