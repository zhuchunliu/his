package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Darren on 2017/11/21.
 */
@Data
public class PermissionMo {
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

}
