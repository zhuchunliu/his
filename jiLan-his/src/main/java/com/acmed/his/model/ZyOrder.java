package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 掌药订单
 * Created by Darren on 2018-04-12
 **/
@Data
@Table(name = "t_zy_order")
@NameStyle
public class ZyOrder {
    @Id
    @ApiModelProperty("id 掌药订单id")
    private String id;

    @ApiModelProperty("订单编号")
    private String orderNo;

    @ApiModelProperty("药品费用")
    private Double drugFee;

    @ApiModelProperty("快递费用")
    private Double expressFee;

    @ApiModelProperty("总费用")
    private Double totalFee;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("地址主键")
    private Integer addressId;

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

    @ApiModelProperty("全地址")
    private String fullAddress;

    @ApiModelProperty("快递主键")
    private String expressId;

    @ApiModelProperty("快递名称")
    private String expressName;

    @ApiModelProperty("快递单号")
    private String expressNo;

    @ApiModelProperty("掌药订单id")
    private String zyOrderId;

    @ApiModelProperty("掌药订单号")
    private String zyOrderSn;

    @ApiModelProperty("药店id - 掌药")
    private String zyStoreId;

    @ApiModelProperty("药店名称 - 掌药")
    private String zyStoreName;

    @ApiModelProperty("支付状态 0:未提交,1:待支付,2:已支付,3:付费失败,4:已退款,5:已经取消")
    private Integer payStatus;

    @ApiModelProperty("支付反馈信息")
    private String payRemark;

    @ApiModelProperty("付费方式 1:微信，2:支付宝")
    private Integer feeType;

    @ApiModelProperty("收货状态 -1：不可收货，无物流信息, 0:待收货；1:已经收货；2:缺货")
    private Integer recepitStatus;

    @ApiModelProperty("提交时间")
    private String submitTime;

    @ApiModelProperty("支付时间")
    private String payTime;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
