package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class AdviceTplMo {
    @ApiModelProperty("医嘱模板id")
    private Integer id;

    @ApiModelProperty("医嘱")
    private String advice;

    @ApiModelProperty("诊断分类:字典中获取")
    private String category;


}
