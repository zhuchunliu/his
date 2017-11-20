package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RoleVsPermission
 * 角色权限表
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_role_vs_permission")
@NameStyle
public class RoleVsPermission {
    @Id
    @ApiModelProperty("权限id")
    private Integer pid;

    @Id
    @ApiModelProperty("角色id")
    private Integer rid;
}
