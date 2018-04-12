package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-04-12
 **/
@Data
public class ZyOrderQueryMo {
    @ApiModelProperty("掌药订单号,药店名称")
    private String name;

    @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
    private Integer payStatus;

    @ApiModelProperty("是否已经收到药 0:否；1:是")
    private Integer isRecepit;
}
