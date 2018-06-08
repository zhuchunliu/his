package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-05-31
 **/
@Data
@Table(name = "t_zy_order_feedback")
@NameStyle
public class ZyOrderFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("his订单id")
    private String hisOrderId;

    @ApiModelProperty("掌药订单id")
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

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
