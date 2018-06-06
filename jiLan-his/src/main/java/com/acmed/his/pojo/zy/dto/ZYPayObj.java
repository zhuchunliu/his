package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-06
 **/
@Data
public class ZYPayObj {
    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("支付总额")
    private Double payPrice;

    @ApiModelProperty("支付链接")
    private String codeUrl;
}
