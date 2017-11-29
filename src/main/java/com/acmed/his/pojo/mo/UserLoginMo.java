package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-29
 **/
@Data
public class UserLoginMo {
    @ApiModelProperty("登录名")
    private String loginName;//登录名
    @ApiModelProperty("密码")
    private String passwd;//密码
}
