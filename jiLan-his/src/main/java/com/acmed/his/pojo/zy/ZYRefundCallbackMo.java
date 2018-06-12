package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-11
 **/
@Data
public class ZYRefundCallbackMo {
    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("退款结果 0:失败,1:成功")
    private Integer refundStatus;

    @ApiModelProperty("成功，失败描述")
    private String remark;
}
