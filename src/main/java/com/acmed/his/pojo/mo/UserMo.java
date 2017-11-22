package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class UserMo {
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("密码")
    private String passWd;

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

    @ApiModelProperty("用户类型")
    private String category;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

}
