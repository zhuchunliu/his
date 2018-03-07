package com.acmed.his.model.dto;

import com.acmed.his.model.Apply;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ApplyDto
 *
 * @author jimson
 * @date 2018/3/7
 */
@Data
public class ApplyDto extends Apply{
    @ApiModelProperty("头像")
    private String avatar;
}
