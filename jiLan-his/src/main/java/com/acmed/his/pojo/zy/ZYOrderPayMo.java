package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-05-24
 **/
@Data
public class ZYOrderPayMo {

    @ApiModelProperty("省份id")
    private String provinceId;

    @ApiModelProperty("城市id")
    private String cityId;

    @ApiModelProperty("县id")
    private String countyId;

    @ApiModelProperty("详细地址")
    private String address;



    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("药品费用")
    private Double drugFee;

    @ApiModelProperty("快递主键")
    private String expressId;
}
