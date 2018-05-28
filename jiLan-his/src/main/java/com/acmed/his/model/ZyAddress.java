package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-05-24
 **/
@Data
@Table(name = "t_zy_address")
@NameStyle
public class ZyAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("省份id")
    private String provinceId;

    @ApiModelProperty("省份名称")
    private String provinceName;

    @ApiModelProperty("省份名称")
    private String cityId;

    @ApiModelProperty("省份名称")
    private String cityName;

    @ApiModelProperty("县主键")
    private String countyId;

    @ApiModelProperty("县名称")
    private String countyName;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("收件人")
    private String recipient;

    @ApiModelProperty("电话")
    private String phone;

    @ApiModelProperty("是否是默认地址 0:否；1:是")
    private Integer isDefault;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
