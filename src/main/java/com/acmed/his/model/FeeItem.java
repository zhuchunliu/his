package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_feeitem")
@NameStyle
public class FeeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("诊疗项目id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("费用类别:使用字典")
    private String feeCategory;

    @ApiModelProperty("项目价格")
    private Float itemPrice;

    @ApiModelProperty("是否有效 0:否;1：是")
    private String isValid;

    @ApiModelProperty("药品备注")
    private String memo;
}
