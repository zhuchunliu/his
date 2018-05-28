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
    private Double fee;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("快递主键")
    private String expressId;

    @ApiModelProperty("掌药订单id")
    private String zyOrderId;

    @ApiModelProperty("掌药订单号")
    private String zyOrderSn;

    @ApiModelProperty("药店id - 掌药")
    private String zyStoreId;

    @ApiModelProperty("药店名称 - 掌药")
    private String zyStoreName;

    @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
    private Integer payStatus;

    @ApiModelProperty("是否已经收到药 0:否；1:是")
    private Integer isRecepit;

    @ApiModelProperty("支付时间")
    private Integer payTime;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
