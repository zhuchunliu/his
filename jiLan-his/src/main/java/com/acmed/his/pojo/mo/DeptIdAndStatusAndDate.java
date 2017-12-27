package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DeptIdAndStatusAndDate
 *
 * @author jimson
 * @date 2017/11/30
 */
@Data
public class DeptIdAndStatusAndDate {
    @ApiModelProperty("科室id")
    private Integer deptId;
    @ApiModelProperty("状态码   不填表示全部付费列表")
    private String status;
    @ApiModelProperty("日期   yyyy-MM-dd")
    private String date;
}
