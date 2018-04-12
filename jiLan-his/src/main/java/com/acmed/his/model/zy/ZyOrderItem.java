package com.acmed.his.model.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

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

    @ApiModelProperty("处方详情id")
    private String itemId;
}
