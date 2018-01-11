package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-21
 **/
@Data
public class UserInfo {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("科室名称")
    private String deptName;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("挂号费")
    private Double applyfee;


}
