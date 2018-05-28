package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-05-24
 **/
@Data
public class ZyAddressMo {

    @ApiModelProperty("id null:新增; not null:编辑")
    private Integer id;

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

    @ApiModelProperty("是否是默认地址 0:否；1:是")
    private Integer isDefault;

    @ApiModelProperty("收件人")
    private String recipient;

    @ApiModelProperty("电话")
    private String phone;
}
