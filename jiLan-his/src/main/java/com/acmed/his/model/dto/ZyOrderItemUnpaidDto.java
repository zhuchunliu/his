package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-01
 **/
@Data
public class ZyOrderItemUnpaidDto {
    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("提交时间")
    private String submitTime;

    @ApiModelProperty("总金额")
    private Double totalFee;

    @ApiModelProperty("操作人")
    private String userName;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("省份名称")
    private String cityName;

    @ApiModelProperty("县名称")
    private String countyName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("收件人")
    private String recipient;

    @ApiModelProperty("邮编编码")
    private String zipCode;

    @ApiModelProperty("电话")
    private String phone;
}
