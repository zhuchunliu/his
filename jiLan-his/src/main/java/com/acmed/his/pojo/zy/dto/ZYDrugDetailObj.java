package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-21
 **/
@Data
public class ZYDrugDetailObj {

    @ApiModelProperty("药品id")
    private String drugId;

    @ApiModelProperty("国药准字号")
    private String number;

    @ApiModelProperty("药品名字")
    private String drugName;

    @ApiModelProperty("药品规格")
    private String form;

    @ApiModelProperty("药企")
    private String companyName;

    @ApiModelProperty("药品图片")
    private String picPath;

    @ApiModelProperty("是否为OTC药品 0为非otc药品 1为otc药品")
    private String OTC;

    @ApiModelProperty("药品成分")
    private String component;

    @ApiModelProperty("疗效")
    private String indication;

    @ApiModelProperty("药品价格")
    private String goodsPrice;

    @ApiModelProperty("药品说明书链接")
    private String url;

    @ApiModelProperty("药品不同方向的图片")
    private List<String> drugPicList;

    @ApiModelProperty("药店信息")
    private ZYDrugStoreDetailObj drugStoreDetailObj;

    @Data
    public static class ZYDrugStoreDetailObj{

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

        @ApiModelProperty("药店公告")
        private String ydNotice;

        @ApiModelProperty("平台公告")
        private String ydNoticePart;
    }

}
