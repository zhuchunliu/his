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
@Table(name = "t_zy_order_item_feedback")
@NameStyle
public class ZyOrderItemFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

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

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
