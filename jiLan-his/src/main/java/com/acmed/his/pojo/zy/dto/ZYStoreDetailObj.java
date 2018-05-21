package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-05-21
 **/
@Data
public class ZYStoreDetailObj {
    @ApiModelProperty("药店id")
    private String storeId;

    @ApiModelProperty("药店所在地区")
    private String city;

    @ApiModelProperty("药店名字")
    private String storeName;

    @ApiModelProperty("药店图片")
    private String storePic;

    @ApiModelProperty("药店经度")
    private String lat;

    @ApiModelProperty("药店纬度")
    private String lng;

    @ApiModelProperty("药店电话")
    private String storeTel;

    @ApiModelProperty("是否在销")
    private String onSale;

}
