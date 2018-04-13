package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 药品零售详情
 *
 * Created by Darren on 2018-04-13
 **/
@Data
@Table(name = "t_b_drug_retail_item")
@NameStyle
public class DrugRetailItem {

    @Id
    @ApiModelProperty("零售详情id")
    private String id;

    @ApiModelProperty("药品零售主键")
    private String retailId;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("挂号费")
    private Double fee;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("备注")
    private String remark;
}
