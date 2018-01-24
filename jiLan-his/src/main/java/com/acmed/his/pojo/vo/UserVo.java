package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-26
 **/
@Data
public class UserVo {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("登录名")
    private String loginName;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("用户类型")
    private String category;

    @ApiModelProperty("状态：0启用,1禁用")
    private String status;

    @ApiModelProperty("头像")
    private String avatar;
}
