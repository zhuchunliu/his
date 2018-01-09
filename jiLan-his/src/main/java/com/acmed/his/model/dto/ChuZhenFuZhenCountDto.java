package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * ChuZhenFuZhenCountDto
 * 初诊复诊统计
 * @author jimson
 * @date 2018/1/9
 */
@Data
public class ChuZhenFuZhenCountDto {
    @ApiModelProperty("初诊数")
    private Integer chuZhen;
    @ApiModelProperty("复诊数")
    private Integer fuZhen;
    @ApiModelProperty("总数")
    private Integer total;
}
