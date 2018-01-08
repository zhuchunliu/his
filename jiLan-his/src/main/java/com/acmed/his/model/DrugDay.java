package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-05
 **/
@Data
@Table(name = "t_r_drug_day")
@NameStyle
public class DrugDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("使用数量")
    private Double num;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("进价")
    private Double bidFee;

    @ApiModelProperty("零售价")
    private Double retailFee;

    @ApiModelProperty("报表日期")
    private String date;
}
