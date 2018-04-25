package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 处方模板详情
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_prescription_tpl_item")
@NameStyle
public class PrescriptionTplItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("详情id")
    private Integer id;

    @ApiModelProperty("处方模板id")
    private Integer tplId;

    @ApiModelProperty("用药id/药品字典id")
    private Integer drugId;

    @ApiModelProperty("单位数量")
    private Integer num;

    @ApiModelProperty("单位类型 1：一级计价单位，2：二级计价单位")
    private Integer unitType;

    @ApiModelProperty("单次剂量")
    private Double singleDose;

    @ApiModelProperty("频率,用药频率  字典表：DrugFrequency")
    private Integer frequency;

    @ApiModelProperty("备注")
    private String memo;
}
