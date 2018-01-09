package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OrgPatientNumDto
 * 患者统计
 * @author jimson
 * @date 2018/1/9
 */
@Data
public class OrgPatientNumDto {
    @ApiModelProperty("今日人数")
    private Integer dayNum;
    @ApiModelProperty("总数")
    private Integer totalNum;
}
