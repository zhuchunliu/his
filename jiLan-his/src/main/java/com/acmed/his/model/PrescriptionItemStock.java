package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-22
 **/
@Data
@Table(name = "t_b_prescription_item_stock")
@NameStyle
public class PrescriptionItemStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("处方明细id")
    private String itemId;

    @ApiModelProperty("有效期")
    private String expiryDate;

    @ApiModelProperty("入库批号")
    private String batchNumber;

    @ApiModelProperty("大单位数量")
    private Integer num;

    @ApiModelProperty("小单位数量")
    private Integer minNum;

    @ApiModelProperty("剂量单位库存数量")
    private Double doseNum;


}
