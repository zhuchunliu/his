package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-05
 **/
@Data
public class ZYExpressCallbackMo {
    @ApiModelProperty("掌药快递单号")
    private String expressNo;

    @ApiModelProperty("掌药订单id")
    private String orderId;
}
