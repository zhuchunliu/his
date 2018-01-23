package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-23
 **/
@Data
public class PrescriptionQueryTplMo {

    @ApiModelProperty("模板名称")
    private String tplName;

    @ApiModelProperty("处方类型  1:药品处方，2：检查处方")
    private String category;

    @ApiModelProperty("模板类型  0:私有；1：共有")
    private String isPublic;
}
