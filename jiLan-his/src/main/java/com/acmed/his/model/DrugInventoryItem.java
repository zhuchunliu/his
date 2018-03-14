package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-03-13
 **/
@Data
@Table(name = "t_b_drug_inventory_item")
@NameStyle
public class DrugInventoryItem {
    @Id
    @ApiModelProperty("药品盘点详情id")
    private String id;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("库存详情id")
    private Integer stockId;

    @ApiModelProperty("库存盘点id")
    private String inventoryId;

    @ApiModelProperty("大单位库存数量")
    private Integer num;

    @ApiModelProperty("小单位库存数量")
    private Integer minNum;

    @ApiModelProperty("剂量单位库存数量")
    private Double doseNum;
}
