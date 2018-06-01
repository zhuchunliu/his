package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-31
 **/
@Data
public class ZYOrderGetObj {

    private ZYOrderGetOrderInfoObj orderInfoObj;

    private ZYOrderGetAddressInfoObj addressInfoObj;

    private List<ZYOrderGetOrderDetailObj> orderDetailObjList;

    @Data
    public class ZYOrderGetOrderInfoObj{

        @ApiModelProperty("订单id")
        private String order_id;

        @ApiModelProperty("订单号")
        private String order_sn;

        @ApiModelProperty("药店id")
        private String store_id;

        @ApiModelProperty("药店名")
        private String store_name;

        @ApiModelProperty("下单时间")
        private String add_time;

        @ApiModelProperty("订单总金额")
        private String order_amount;

        @ApiModelProperty("订单状态 10-未付款 20-已付款")
        private String order_state;

        @ApiModelProperty("支付方式 1-微信 2-支付宝")
        private String payment_id;

        @ApiModelProperty("药店电话")
        private String storeTel;

    }

    @Data
    public class ZYOrderGetAddressInfoObj{

        @ApiModelProperty("收货人姓名")
        private String true_name;

        @ApiModelProperty("省份id")
        private String province_id;

        @ApiModelProperty("市id")
        private String city_id;

        @ApiModelProperty("县id")
        private String area_id;

        @ApiModelProperty("省市县")
        private String area_info;

        @ApiModelProperty("详细地址")
        private String address;

        @ApiModelProperty("收货人电话")
        private String mob_phone;

        @ApiModelProperty("配送费用")
        private String shipping_fee;

        @ApiModelProperty("配送方式")
        private String shipping_name;

        @ApiModelProperty("运费减免金额")
        private String shipping_minus_fee;

        @ApiModelProperty("用户选择的快递方式")
        private String exp_remark;

    }

    @Data
    public class ZYOrderGetOrderDetailObj{

        @ApiModelProperty("订单id")
        private String order_id;

        @ApiModelProperty("药品id")
        private String goods_id;

        @ApiModelProperty("规格")
        private String spec_info;

        @ApiModelProperty("药品价格")
        private String goods_price;

        @ApiModelProperty("药品数量")
        private String goods_num;

        @ApiModelProperty("药品图片")
        private String goods_image;

    }
}
