package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-09
 **/
@Data
public class DrugDayDetailDto {
    @ApiModelProperty("药品名称")
    private String drugName;

    @ApiModelProperty("销售数量")
    private Double num;

    @ApiModelProperty("日期")
    private String date;
}
