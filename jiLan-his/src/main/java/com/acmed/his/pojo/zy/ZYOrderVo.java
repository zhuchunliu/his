package com.acmed.his.pojo.zy;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-04-12
 **/
@Data
public class ZYOrderVo {
    @ApiModelProperty("id 掌药订单id")
    private String id;

    @ApiModelProperty("掌药订单号")
    @JSONField(name = "orderSn")
    private String zyOrderSn;

    @ApiModelProperty("药店名称 - 掌药")
    @JSONField(name = "storeName")
    private String zyStoreName;

    @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
    private Integer payStatus;

    @ApiModelProperty("是否已经收到药 0:否；1:是")
    private Integer isRecepit;

    @ApiModelProperty("支付时间")
    private Integer payTime;
}
