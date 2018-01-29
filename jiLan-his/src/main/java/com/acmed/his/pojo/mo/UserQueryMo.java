package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-26
 **/
@Data
public class UserQueryMo {

    @ApiModelProperty("用户名")
    private String userName;

    @ApiModelProperty("手机号")
    private String mobile;

    @ApiModelProperty("科室id")
    private Integer deptId;

    @ApiModelProperty("状态 0:禁用，1：启用")
    private String status;
}
