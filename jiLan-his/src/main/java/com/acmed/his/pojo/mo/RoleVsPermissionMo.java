package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * Created by Darren on 2017-11-26
 **/
@Data
public class RoleVsPermissionMo {


    @ApiModelProperty("角色id")
    private Integer rid;

    @ApiModelProperty("角色名称")
    private String roleName;
}
