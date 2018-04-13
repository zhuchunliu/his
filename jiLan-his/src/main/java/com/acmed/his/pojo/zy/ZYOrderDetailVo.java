package com.acmed.his.pojo.zy;

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-04-12
 **/
@Data
public class ZYOrderDetailVo {
    @ApiModelProperty(value = "订单Id",hidden = true)
    private String orderId;

    @ApiModelProperty("订单号")
    private String orderSn;

    @ApiModelProperty("支付总额")
    private String payAmount;

    @ApiModelProperty("支付时间")
    private String payTime;

    @ApiModelProperty("订单状态 10-未支付 20-已支付")
    private String orderState;

    @ApiModelProperty("用户手机号")
    private String phone;

    private List<ZYOrderDetailVo.ZYOrderDetailItemVo> goodsList;

    @Data
    public static class ZYOrderDetailItemVo{
        @ApiModelProperty("商品名称")
        @JSONField(name="drugName")
        private String cnName;

        @ApiModelProperty("商品数量")
        @JSONField(name="num")
        private String goodsNum;

        @ApiModelProperty("商品单价")
        @JSONField(name="retailPrice")
        private String goodsPrice;

    }
}
