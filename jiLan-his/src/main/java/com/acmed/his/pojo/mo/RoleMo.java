package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class RoleMo {
    @Id
    private Integer id;

//    @ApiModelProperty("权限组编码")
//    private String roleCode;

    @ApiModelProperty("权限组名称")
    private String roleName;

    @ApiModelProperty("权限组描述")
    private String roleDesc;
}
