package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by Darren on 2017-12-28
 **/
@Data
public class DrugDictVo {

    @ApiModelProperty("药品id")
    private String id;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("药品分类名称 ")
    private String categoryName;

    @ApiModelProperty("剂型")
    private String drugFormName;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("准字号")
    private String approvalNumber;

    @ApiModelProperty("条形码")
    private String barcode;

    @ApiModelProperty("单位（g/条）大单位 ")
    private String unitName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("药品说明")
    private String instructions;
}
