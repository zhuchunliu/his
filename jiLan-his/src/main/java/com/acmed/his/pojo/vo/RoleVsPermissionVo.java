package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-01-29
 **/
@Data
public class RoleVsPermissionVo {
    @ApiModelProperty("权限名称")
    private String perName;

    private List<RoleVsPermissionVo.PermissionChild> list;
    @Data
    public class PermissionChild {

        @ApiModelProperty("id")
        private Integer id;

        @ApiModelProperty("权限名称")
        private String perName;

        @ApiModelProperty("是否选中 0:未选中，1：已被选中")
        private String isChecked;
    }
}
