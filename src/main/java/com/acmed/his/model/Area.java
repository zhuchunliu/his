package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Area
 * 行政区划分
 *
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_area")
@NameStyle
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("父id")
    private Integer pid;

    @ApiModelProperty("简称")
    private String shortName;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("全称")
    private String fullName;

    @ApiModelProperty("层级 0 1 2 省市区县")
    private Integer level;

    @ApiModelProperty("拼音")
    private String pinYin;

    @ApiModelProperty("长途区号")
    private String code;

    @ApiModelProperty("邮编")
    private String zipCode;

    @ApiModelProperty("首字母")
    private String first;

    @ApiModelProperty("精度")
    private String lng;

    @ApiModelProperty("维度")
    private String lat;
}
