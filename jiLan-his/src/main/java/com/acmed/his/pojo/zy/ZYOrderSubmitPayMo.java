package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-24
 **/
@Data
public class ZYOrderSubmitPayMo {

    @ApiModelProperty("省份id")
    private String provinceId;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("省份名称")
    private String cityId;

    @ApiModelProperty("省份名称")
    private String cityName;

    @ApiModelProperty("县主键")
    private String countyId;

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

    @ApiModelProperty("付费方式 1:微信，2:支付宝")
    private Integer feeType;

    @ApiModelProperty("详情")
    private List<ZYOrderSubmitPayDetailMo> detailMoList;

    @Data
    public class ZYOrderSubmitPayDetailMo {

        @ApiModelProperty("订单id")
        private String orderId;

        @ApiModelProperty("快递主键")
        private String expressId;

        @ApiModelProperty("快递费用")
        private Double expressFee;

        @ApiModelProperty("快递名称")
        private String expressName;

        @ApiModelProperty("详情主键")
        private List<String> itemIdList;
    }
}
