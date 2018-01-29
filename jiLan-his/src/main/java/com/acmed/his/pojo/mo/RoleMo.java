package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class RoleMo {
    @ApiModelProperty("权限id null：添加，not null：编辑")
    private Integer id;

    @ApiModelProperty("权限组名称")
    private String roleName;

    @ApiModelProperty("权限组描述")
    private String roleDesc;

    @ApiModelProperty("权限id集合,逗号间隔")
    private String pids;
}
