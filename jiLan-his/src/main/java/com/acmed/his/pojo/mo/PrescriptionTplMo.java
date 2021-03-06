package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class PrescriptionTplMo {

    @ApiModelProperty("模板id null：新增，not null:编辑")
    private Integer id;

    @ApiModelProperty("模板名称")
    private String tplName;

    @ApiModelProperty("处方类型  1:药品处方，2：检查处方")
    private String category;

    @ApiModelProperty("模板类型  0:私有；1：共有")
    private String isPublic;

    @ApiModelProperty("模板说明")
    private String description;

}
