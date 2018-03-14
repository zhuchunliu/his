package com.acmed.his.pojo.vo;

import com.acmed.his.model.Permission;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-31
 **/
@Data
public class MenuVo {
    @ApiModelProperty("菜单名称")
    private String menuName;

    @ApiModelProperty("菜单编码,即：中文拼音")
    private String menuCode;

    public MenuVo(Permission permission) {
        this.menuName = permission.getPerName();
        this.menuCode = permission.getPerCode();
    }

    public MenuVo(String menuName,String menuCode) {
        this.menuName = menuName;
        this.menuCode = menuCode;
    }
}
