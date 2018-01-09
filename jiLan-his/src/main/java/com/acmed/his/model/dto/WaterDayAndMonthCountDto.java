package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * WaterDayAndMonthCount
 * 昨日收益  本月收益
 * @author jimson
 * @date 2018/1/9
 */
@Data
public class WaterDayAndMonthCountDto {
    @ApiModelProperty("机构编号")
    private Integer orgCode;
    @ApiModelProperty("昨日收益")
    private Integer dayTotal;
    @ApiModelProperty("本月收益")
    private Integer monthTotal;
}
