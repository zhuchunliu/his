package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 掌药订单详情
 * Created by Darren on 2018-04-12
 **/
@Data
@Table(name = "t_zy_order_item")
@NameStyle
public class ZyOrderItem {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("掌药订单id")
    private String orderId;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("处方详情id")
    private String preItemId;

    @ApiModelProperty("药品id")
    private Integer drugId;

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

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;
}
