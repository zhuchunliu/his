package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-18
 **/
@Data
@Table(name = "t_b_prescription_fee")
@NameStyle
public class PrescriptionFee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("挂号单id")
    private String applyId;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("应收款")
    private Double receivables;

    @ApiModelProperty("实收款")
    private Double receipts;

    @ApiModelProperty("已退款")
    private Double refunded;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
