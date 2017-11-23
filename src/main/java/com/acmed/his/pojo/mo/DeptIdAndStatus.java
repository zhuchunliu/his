package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DeptIdAndStatus
 *
 * @author jimson
 * @date 2017/11/22
 */
@Data
public class DeptIdAndStatus {
    @ApiModelProperty("科室id")
    private Integer deptId;
    @ApiModelProperty("状态码   不填表示全部付费列表")
    private String status;
}
