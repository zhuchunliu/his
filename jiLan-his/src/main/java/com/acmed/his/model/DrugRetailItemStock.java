package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 零售对应药品批次
 *
 * Created by Darren on 2018-04-13
 **/
@Data
@Table(name = "t_b_drug_retail_item_stock")
@NameStyle
public class DrugRetailItemStock {

    @Id
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("药品零售主键")
    private String retailId;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("有效期")
    private String expiryDate;

    @ApiModelProperty("入库批号")
    private String batchNumber;

    @ApiModelProperty("大单位数量")
    private Integer num;
}
