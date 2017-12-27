package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-26
 **/
@Data
public class UserVsRoleMo {
    @ApiModelProperty("用户id")
    private Integer uid;

    @ApiModelProperty("角色id集合,逗号间隔")
    private String rids;
}
