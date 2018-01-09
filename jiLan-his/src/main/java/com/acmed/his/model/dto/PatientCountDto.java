package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PatientCountDto
 *
 * @author jimson
 * @date 2018/1/9
 */
@Data
public class PatientCountDto {
    @ApiModelProperty("数量")
    private Integer num;
    @ApiModelProperty("年龄分类的数字类型 如1 2 34 5 ")
    private Integer quarter;
    @ApiModelProperty("数字类型对应的解释  比如儿童  少年  青年 中年 老年")
    private String quarterStr;
}
