package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-12
 **/
@Data
public class ChiefComplaintTplMo {
    @ApiModelProperty("主述模板id")
    private Integer id;

    @ApiModelProperty("主述")
    private String chiefComplaint;

    @ApiModelProperty("模板名称")
    private String tplName;

//    @ApiModelProperty("诊断分类:字典中获取")
//    private String category;

//    @ApiModelProperty("备注")
//    private String memo;

    @ApiModelProperty("模板类型  0:私有；1：共有")
    private String isPublic;
}
