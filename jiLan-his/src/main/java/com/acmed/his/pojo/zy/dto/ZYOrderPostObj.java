package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 掌药下单提交对象
 * Created by Darren on 2018-05-31
 **/
@Data
public class ZYOrderPostObj {

    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("配送方式 45-快递，49-到店自提")
    private String deliverId;

    @ApiModelProperty("订单总金额（商品总额+运费）包邮或者到店自提无需加上邮费,后台会重新计算总额然后和传入的总额进行比对")
    private Double total;

    @ApiModelProperty("快递id")
    private Double expId;

    @ApiModelProperty("省份id")
    private Double provinceId;

    @ApiModelProperty("市id")
    private Double areaId;

    @ApiModelProperty("省市区")
    private Double areaInfo;

    @ApiModelProperty("详细地址")
    private Double address;

    @ApiModelProperty("收件人姓名")
    private Double trueName;

    @ApiModelProperty("收件人手机号")
    private Double mobPhone;

    @ApiModelProperty("下单药品")
    private List<ZYOrderPostDetailObj> drugList;


    @Data
    public class ZYOrderPostDetailObj{
        @ApiModelProperty("药品主键")
        private Integer drugId;

        @ApiModelProperty("药品数量")
        private Integer drugNum;
    }
}
