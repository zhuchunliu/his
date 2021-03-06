package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-06-01
 **/
@Data
public class UnpaidOrderVo {

    @ApiModelProperty("订单id")
    private String orderId;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("药品费用")
    private Double drugFee;

    @ApiModelProperty("快递费用")
    private Double expressFee;

    @ApiModelProperty("店铺满fullReduceFee免邮")
    private Double fullReduceFee;

    @ApiModelProperty("快递主键")
    private String expressId;

    @ApiModelProperty("快递名称")
    private String expressName;


    private List<UnpaidOrderVo.UnpaidDetailVo> detailVoList;

    @Data
    public static class UnpaidDetailVo{

        @ApiModelProperty("详情id")
        private String itemId;

        @ApiModelProperty("挂号主键")
        private String applyId;

        @ApiModelProperty("处方编号,医疗机构id+排序")
        private String prescriptionNo;

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("零售价")
        private Double retailPrice;

        @ApiModelProperty("费用")
        private Double fee;

        @ApiModelProperty("药品厂家 - 掌药")
        private String manufacturerName;
    }
}
