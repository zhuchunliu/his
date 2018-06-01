package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-05-31
 **/
@Data
@Table(name = "t_zy_address_feedback")
@NameStyle
public class ZyAddressFeedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("收货人姓名")
    private String trueName;

    @ApiModelProperty("省份id")
    private String provinceId;

    @ApiModelProperty("市id")
    private String cityId;

    @ApiModelProperty("县id")
    private String areaId;

    @ApiModelProperty("省市县")
    private String areaInfo;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("收货人电话")
    private String mobPhone;

    @ApiModelProperty("配送费用")
    private String shippingFee;

    @ApiModelProperty("配送方式")
    private String shippingName;

    @ApiModelProperty("运费减免金额")
    private String shippingMinusFee;

    @ApiModelProperty("用户选择的快递方式")
    private String expRemark;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
