package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class UserMo implements Serializable{
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("手机")
    private String mobile;

    @ApiModelProperty("用户名")
    private String userName;

//    @ApiModelProperty("密码")
//    private String passWd;

    @ApiModelProperty("性别")
    private String gender;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty(value = "机构名称",hidden = true)
    private String orgName;

    @ApiModelProperty(value = "科室名称",hidden = true)
    private String deptName;

    @ApiModelProperty("科室")
    private Integer dept;

    @ApiModelProperty("用户类型")
    private String category;

    @ApiModelProperty("状态：0启用,1禁用")
    private String status;

}
