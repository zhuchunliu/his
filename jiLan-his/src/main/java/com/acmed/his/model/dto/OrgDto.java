package com.acmed.his.model.dto;

import com.acmed.his.model.Org;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OrgDto
 *
 * @author jimson
 * @date 2018/3/6
 */
@Data
public class OrgDto extends Org{
    @ApiModelProperty("等级")
    private String levelStr;
    @ApiModelProperty("类型")
    private String categoryStr;
}
