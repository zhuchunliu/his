package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-05-21
 **/
@Data
public class ZYCityObj {
    @ApiModelProperty("省id")
    private String provinceId;

    @ApiModelProperty("省名称")
    private String provinceName;

    @ApiModelProperty("市/县、区/街道、乡镇id")
    private String areaId;

    @ApiModelProperty("市/县、区/街道、乡镇名称")
    private String areaName;

    @ApiModelProperty("市/县、区/街道、乡镇简称")
    private String shortName;
}
