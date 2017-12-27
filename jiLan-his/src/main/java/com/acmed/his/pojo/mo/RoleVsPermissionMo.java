package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * Created by Darren on 2017-11-26
 **/
@Data
public class RoleVsPermissionMo {
    @ApiModelProperty("权限id集合,逗号间隔")
    private String pids;

    @ApiModelProperty("角色id")
    private Integer rid;
}
