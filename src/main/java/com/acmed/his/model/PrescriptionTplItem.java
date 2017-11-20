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

    @ApiModelProperty("用药id")
    private Integer drugId;

    @ApiModelProperty("药品类型")
    private String drugCategory;

    @ApiModelProperty("药品名称")
    private String drugName;

    @ApiModelProperty("单次剂量")
    private Integer dose;

    @ApiModelProperty("途径 用法")
    private String way;

    @ApiModelProperty("频率,用药频率")
    private Float frequency;

    @ApiModelProperty("天数")
    private Integer num;

    @ApiModelProperty("疗程")
    private Integer course;

    @ApiModelProperty("备注")
    private String memo;
}
