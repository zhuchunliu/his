package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class BsgOrgMo {
    @ApiModelProperty("机构code 新增不传")
    private Integer orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("机构类型 字典表：OrgCategory")
    private String category;

    @ApiModelProperty("医疗机构类别：三级甲等,三级乙等.....字典表：OrgLevel")
    private String level;

    @ApiModelProperty("联系人")
    private String linkMan;

    @ApiModelProperty("联系人手机")
    private String mobile;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("县")
    private String country;

    @ApiModelProperty("医院简介")
    private String introduction;

    @ApiModelProperty("诊所联系电话")
    private String telephone;

    @ApiModelProperty("医院图片")
    private String imgUrl;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("纬度")
    private String lat;
}
