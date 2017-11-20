package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Role
 * 角色表
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_role")
@NameStyle
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("权限组编码")
    private String roleCode;

    @ApiModelProperty("权限组名称")
    private String roleName;

    @ApiModelProperty("权限组描述")
    private String roleDesc;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    private String operatorUserId;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("修改时间")
    private Date modifyTime;
}
