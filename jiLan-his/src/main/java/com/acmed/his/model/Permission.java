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
 * PermissionMapper
 * 权限列表
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_permission")
@NameStyle
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("权限编码")
    private String perCode;

    @ApiModelProperty("权限名称")
    private String perName;

    @ApiModelProperty("描述")
    private String perDesc;

    @ApiModelProperty("排序")
    private Integer sn;

    @ApiModelProperty("父id")
    private Integer pid;

    @ApiModelProperty("类型:菜单,操作权限，按钮")
    private String category;

    @ApiModelProperty("请求路径")
    private String url;

    @ApiModelProperty("操作人")
    private String operatorUserId;

    @ApiModelProperty("创建时间")
    private String createTime;

    @ApiModelProperty("修改时间")
    private String modifyTime;
}
