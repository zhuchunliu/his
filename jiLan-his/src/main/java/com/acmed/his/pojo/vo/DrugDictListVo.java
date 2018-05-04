package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;

/**
 * Created by Darren on 2018-03-22
 **/
@Data
public class DrugDictListVo {
    @Id
    @ApiModelProperty("药品字典id")
    private String id;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("是否是国家基本药物  1 是 2 否")
    private Integer isEssential;

    @ApiModelProperty("药品分类名称")
    private String categoryName;

    @ApiModelProperty("处方类型")
    private String prescriptionTypeName;

    @ApiModelProperty("剂型名称")
    private String drugFormName;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("使用方法名称")
    private String useageName;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("是否处理过0:否，1:是")
    private Integer isHandle;

}
