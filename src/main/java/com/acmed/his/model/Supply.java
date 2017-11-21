package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Supply
 * 供应商
 * @author jimson
 * @date 2017/11/21
 */
@Data
@Table(name = "t_b_supply")
@NameStyle
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("供货商名称")
    private String supplyerName;

    @ApiModelProperty("简名")
    private String abbrName;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("联系人")
    private String linkMan;

    @ApiModelProperty("拼音")
    private String pinYin;

    @ApiModelProperty("联系人手机")
    private String mobile;

    @ApiModelProperty("业务范围")
    private String busiscope;

    @ApiModelProperty("备注")
    private String comment;
}
