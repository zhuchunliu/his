package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class DiagnosisTplMo {
    @ApiModelProperty("诊疗项目id")
    private Integer id;

    @ApiModelProperty("诊断分类:字典中获取")
    private String category;

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("药品备注")
    private String memo;

    @ApiModelProperty("模板类型  0:私有；1：共有")
    private String isPublic;

}
