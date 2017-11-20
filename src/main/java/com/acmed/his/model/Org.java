package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Org
 * 医疗机构
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_org")
@NameStyle
public class Org {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @ApiModelProperty("省")
    private String province;

    @ApiModelProperty("市")
    private String city;

    @ApiModelProperty("县")
    private String country;

    @ApiModelProperty("状态：0启用,1停止")
    private String status;

    @ApiModelProperty("经度")
    private String lng;

    @ApiModelProperty("纬度")
    private String lat;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("修改时间")
    private Date modifyAt;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;

    @ApiModelProperty("截止时间")
    private Date expireTime;

    @ApiModelProperty("代理商")
    private String supply;

    @ApiModelProperty("是否就医北上广医院")
    private String isRecommend;
}
