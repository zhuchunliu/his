package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 药品生产厂商
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_manufacturer")
@NameStyle
public class Manufacturer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("厂商编码")
    private Integer id;

    @ApiModelProperty("厂商名称")
    private String name;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("生产范围")
    private String scope;

    @ApiModelProperty("许可编号")
    private String licenceNo;
}
