package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-07
 **/
@Data
public class ZYPayCallbackMo {
    @ApiModelProperty("订单id")
    private String[] orderIds;

    @ApiModelProperty("付款结果 0:失败,1:成功")
    private Integer payStatus;

    @ApiModelProperty("成功，失败描述")
    private String remark;
}
