package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-31
 **/
@Data
public class ZYOrderGetObj {

    private ZYOrderGetOrderInfoObj orderInfo;

    private ZYOrderGetAddressInfoObj addressInfo;

    private List<ZYOrderGetOrderDetailObj> orderDetail;

    @Data
    public static class ZYOrderGetOrderInfoObj{

        @ApiModelProperty("订单id")
        private String orderId;

        @ApiModelProperty("订单号")
        private String orderSn;

        @ApiModelProperty("药店id")
        private String storeId;

        @ApiModelProperty("药店名")
        private String storeName;

        @ApiModelProperty("药店电话")
        private String storePhone;

        @ApiModelProperty("下单时间")
        private String addTime;

        @ApiModelProperty("订单总金额")
        private String orderAmount;

        @ApiModelProperty("订单状态 10-未付款 20-已付款")
        private String orderState;

        @ApiModelProperty("支付方式 1-微信 2-支付宝")
        private String paymentId;


    }

    @Data
    public static class ZYOrderGetAddressInfoObj{

        @ApiModelProperty("订单id")
        private String orderId;

        @ApiModelProperty("收货人姓名")
        private String trueName;

        @ApiModelProperty("省份id")
        private String provinceId;

        @ApiModelProperty("市id")
        private String cityId;

        @ApiModelProperty("县id")
        private String areaId;

        @ApiModelProperty("省市县")
        private String areaInfo;

        @ApiModelProperty("详细地址")
        private String address;

        @ApiModelProperty("收货人电话")
        private String mobPhone;

        @ApiModelProperty("配送费用")
        private String shippingFee;

        @ApiModelProperty("配送方式id")
        private String shippingId;

        @ApiModelProperty("配送方式")
        private String shippingName;

        @ApiModelProperty("运费减免金额")
        private String shippingMinusFee;

        @ApiModelProperty("用户选择的快递方式")
        private String expRemark;

    }

    @Data
    public static class ZYOrderGetOrderDetailObj{

        @ApiModelProperty("订单id")
        private String orderId;

        @ApiModelProperty("药品id")
        private String goodsId;

        @ApiModelProperty("药品名称")
        private String goodsName;

        @ApiModelProperty("规格")
        private String specInfo;

        @ApiModelProperty("药品价格")
        private String goodsPrice;

        @ApiModelProperty("药品数量")
        private String goodsNum;

        @ApiModelProperty("药品图片")
        private String goodsImage;

    }
}
