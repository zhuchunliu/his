package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-23
 **/
@Data
public class PrescriptionTplDto {

    @ApiModelProperty("模板id")
    private Integer id;

    @ApiModelProperty("模板名称")
    private String tplName;

    @ApiModelProperty("处方类型   1:药品处方，2：检查处方")
    private String category;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("是否公开 0:否；1：是")
    private String isPublic;

    @ApiModelProperty("是否有效 0:无；1：有")
    private String isValid;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否是自己的模板 0：否 1:是")
    private String isSelf;


}
