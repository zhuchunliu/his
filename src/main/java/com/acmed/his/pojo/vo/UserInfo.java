package com.acmed.his.pojo.vo;/**
 * Created by Eric on 2017-05-23.
 */


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.Serializable;

/**
 * 用户信息DTO
 *
 * @Author Eric
 * @Version 2017-05-23 17:12
 **/

@Log4j
public @Data
class UserInfo implements Serializable {
    @ApiModelProperty(value="用户id")
    private String id;
    @ApiModelProperty(value="姓名")
    private String username; //昵称
    @ApiModelProperty(value="昵称")
    private String nickname; //昵称
    @ApiModelProperty(value="用户类别")
    private String category; // 用户类别
    @ApiModelProperty(value="用户手机号")
    private String mobile;//用户手机号
    @ApiModelProperty(value="用户性别")
    private String gender; // 用户性别
    @ApiModelProperty(value="状态")
    private String status;//状态
    @ApiModelProperty(value="机构id")
    private String orgCode;//机构id
    @ApiModelProperty(value="机构名称")
    private String orgName;//机构名称
    @ApiModelProperty(value="科室id")
    private String deptId;//科室id
    @ApiModelProperty(value="科室名称")
    private String deptName;//科室名称
    @ApiModelProperty(value="用户生成的token")
    private String token;//用户生成的token

    @ApiModelProperty(value = "密码")
    private String passwd;
}
