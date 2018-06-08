package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-06
 **/
@Data
public class ZyOrderItemUnpaidDetailDto {
    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("药品费用")
    private Double drugFee;

    @ApiModelProperty("快递费用")
    private Double expressFee;

    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("快递主键")
    private String expressId;

    @ApiModelProperty("快递名称")
    private String expressName;


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

    @ApiModelProperty("待支付展示状态：0:正常，1:新增的，2:被删除的，4:不展示的")
    private Integer status;
}
