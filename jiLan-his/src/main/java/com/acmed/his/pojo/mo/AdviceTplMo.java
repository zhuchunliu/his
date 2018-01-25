package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

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

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("模板类型  0:私有；1：共有")
    private String isPublic;
}
