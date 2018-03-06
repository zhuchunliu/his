package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AppraiseAccompanyingOrderModel
 *
 * @author jimson
 * @date 2018/3/6
 */
@Data
public class AppraiseAccompanyingOrderModel {
    @ApiModelProperty("订单号")
    private String orderCode;
    @ApiModelProperty("评分")
    private Double point;
}

