package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class OrgMo {
    @ApiModelProperty("机构code")
    private Integer orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构管理员")
    private String manager;

    @ApiModelProperty("机构类型")
    private String category;

    @ApiModelProperty("医疗机构类别：三级甲等,三级乙等.....")
    private String level;

    @ApiModelProperty("联系人")
    private String linkMan;

    @ApiModelProperty("联系人手机")
    private String mobile;

    @ApiModelProperty("地址")
    private String address;

//    @ApiModelProperty("省")
//    private String province;
//
//    @ApiModelProperty("市")
//    private String city;
//
//    @ApiModelProperty("县")
//    private String country;

    @ApiModelProperty("状态 0:启用，1：禁用")
    private String status;

    @ApiModelProperty("医院简介")
    private String introduction;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("截止时间")
    private String expireTime;

    @ApiModelProperty("代理商")
    private String supply;

    @ApiModelProperty("是否就医北上广医院")
    private String isRecommend;
}
