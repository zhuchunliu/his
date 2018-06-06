package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-05
 **/
@Data
public class ZyDispenseOrderDetailDto {

    @ApiModelProperty("用药名称")
    private String drugName;

    @ApiModelProperty("药品厂家 - 掌药")
    private String manufacturerName;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("掌药订单id")
    private String orderId;

    @ApiModelProperty("快递名称")
    private String expressName;

    @ApiModelProperty("快递单号")
    private String expressNo;

    @ApiModelProperty("0:未收货,1:已收货")
    private Integer isRecepit;

}
