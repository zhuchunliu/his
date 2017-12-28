package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-28
 **/
@Data
public class DrugVo {

    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品；4：诊断试剂")
    private String category;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("计价单位")
    private String unit;

    @ApiModelProperty("库存数量")
    private String num;

    @ApiModelProperty("包装单位")
    private String packUnit;

}
