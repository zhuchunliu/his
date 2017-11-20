package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 处方详情
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_prescription_item")
@NameStyle
public class PrescriptionItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("用药id")
    private Integer id;

    @ApiModelProperty("处方id")
    private Integer prescriptionId;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("药品类型")
    private String category;

    @ApiModelProperty("用药名称")
    private String drugName;

    @ApiModelProperty("途径")
    private String way;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("频率")
    private Integer frequency;

    @ApiModelProperty("疗程")
    private Integer course;

    @ApiModelProperty("费用")
    private Double fee;
}
