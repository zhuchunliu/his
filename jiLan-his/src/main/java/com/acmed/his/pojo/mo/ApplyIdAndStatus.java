package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ApplyIdAndStatus
 *
 * @author jimson
 * @date 2017/11/22
 */
@Data
public class ApplyIdAndStatus {
    @ApiModelProperty("挂号单id")
    private String id;
    @ApiModelProperty("状态 0:未就诊;1:已就诊,2:已取消")
    private String status;
}
