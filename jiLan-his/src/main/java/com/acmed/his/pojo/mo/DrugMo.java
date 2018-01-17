package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-28
 **/
@Data
public class DrugMo {
    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品；4：诊断试剂")
    private String category = "0";

    @ApiModelProperty("药品类型 0:OTC药品; 1:处方药品")
    private String classification;

    @ApiModelProperty("包装单位")
    private String packUnit;

    @ApiModelProperty("计价单位")
    private String unit;

    @ApiModelProperty("包装数量")
    private Integer packNum;

    @ApiModelProperty("剂型")
    private String drugForm;

    @ApiModelProperty("生产厂家")
    private Integer manufacturer;

    @ApiModelProperty("使用方法")
    private String useage;

    @ApiModelProperty("加成率")
    private Double markonpercent;

    @ApiModelProperty("药品备注")
    private String memo;

}
