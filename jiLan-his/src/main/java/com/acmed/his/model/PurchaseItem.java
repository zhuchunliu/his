package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-03
 **/
@Data
@Table(name = "t_b_purchase_item")
@NameStyle
public class PurchaseItem {
    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("批次库存ID")
    private String purchaseId;

    @ApiModelProperty("采购数量")
    private Integer num;

    @ApiModelProperty("采购单位")
    private String unit;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("采购批发总额")
    private Double bidFee;

    @ApiModelProperty("采购处方总额")
    private Double retailFee;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;

}
