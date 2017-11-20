package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * UserVsRole
 * 用户角色表
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_user_vs_role")
@NameStyle
public class UserVsRole {
    @Id
    @ApiModelProperty("用户id")
    private Integer uid;

    @Id
    @ApiModelProperty("角色id")
    private Integer rid;
}
