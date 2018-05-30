package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-05-21
 **/
@Data
public class ZYDrugDetailVo {
    @ApiModelProperty("药品id")
    private String drugId;

    @ApiModelProperty("药品名字")
    private String drugName;

    @ApiModelProperty("生产厂家")
    private String manufacturerName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("国药准字号")
    private String approvalNumber;

    @ApiModelProperty("药品价格")
    private String retailPrice;

    @ApiModelProperty("所在城市")
    private String city;

    @ApiModelProperty("药品图片")
    private String picPath;

    @ApiModelProperty("药品成分")
    private String component;

    @ApiModelProperty("疗效")
    private String indication;

    @ApiModelProperty("药品说明书链接")
    private String url;

    @ApiModelProperty("药品不同方向的图片")
    private List<String> drugPicList;


}
