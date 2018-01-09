package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-09
 **/
@Data
public class InspectDayDto {

    @ApiModelProperty("检查次数")
    private Integer num;

    @ApiModelProperty("检查项目")
    private String inspectName;

    @ApiModelProperty("收费金额")
    private Double fee;

    @ApiModelProperty("检查时间")
    private String date;
}
