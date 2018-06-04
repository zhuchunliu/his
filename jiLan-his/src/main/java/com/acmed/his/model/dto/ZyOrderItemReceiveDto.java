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

    @ApiModelProperty("药品费用")
    private Double drugFee;

    @ApiModelProperty("快递费用")
    private Double expressFee;

    @ApiModelProperty("总费用")
    private Double totalFee;

    @ApiModelProperty("提交时间")
    private String submitTime;

    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("支付状态 0:未提交,1:待支付,2:已支付,3:已退款,4:已经取消")
    private Integer payStatus;

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
