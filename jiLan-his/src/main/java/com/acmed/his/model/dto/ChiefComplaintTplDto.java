package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-12
 **/
@Data
public class ChiefComplaintTplDto {
    @ApiModelProperty("主述模板id")
    private Integer id;

    @ApiModelProperty("模板名称")
    private String tplName;

//    @ApiModelProperty("医嘱类型")
//    private String category;
//
//    @ApiModelProperty("医嘱类型名称")
//    private String categoryName;

    @ApiModelProperty("是否有效 0:无；1：有")
    private String isValid;

    @ApiModelProperty("是否公开 0:否；1：是")
    private String isPublic;

    @ApiModelProperty("是否是自己的模板 0：否 1:是")
    private String isSelf;

    @ApiModelProperty("主述")
    private String chiefComplaint;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private String createAt;
}
