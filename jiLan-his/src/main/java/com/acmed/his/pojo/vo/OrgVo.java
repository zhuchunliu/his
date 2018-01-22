package com.acmed.his.pojo.vo;

import com.acmed.his.model.Dept;
import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2017-12-06
 **/
@Data
public class OrgVo {
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

    @ApiModelProperty("状态 0:启用，1：禁用")
    private String status;

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

    @ApiModelProperty("医院简介")
    private String introduction;

    @ApiModelProperty(value = "距离 单位：km,未格式化",hidden = true)
    @JSONField(serialize = false)
    private Double distanceUn;

    @ApiModelProperty("挂号量")
    private Integer applyNum;

    @ApiModelProperty("距离 单位：km")
    private String distance;

    @ApiModelProperty("职务")
    private String position;

    @ApiModelProperty("诊所联系电话")
    private String telephone;

    @ApiModelProperty("科室列表")
    private List<Dept> deptList;

    @ApiModelProperty("医院图片")
    private String imgUrl;
}
