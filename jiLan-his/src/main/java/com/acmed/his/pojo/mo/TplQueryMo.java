package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 模板
 *
 * Created by Darren on 2018-01-25
 **/
@Data
public class TplQueryMo {

    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("模板类型  0:私有；1：共有")
    private Integer isPublic;

    @ApiModelProperty("是否有效 0:无；1：有")
    private String isValid;
}
