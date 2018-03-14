package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-14
 **/
@Data
public class DrugWarnVo {

    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("生产厂家")
    private String manufacturerName;

    @ApiModelProperty("库存数量名称")
    private String numName;

    @ApiModelProperty("过期数量名称")
    private String expireNumName;

    @ApiModelProperty("安全库存数量名称")
    private String safetyNumName;

    @ApiModelProperty("预警类型 1：库存不足，2：过期提醒")
    private Integer warnType;
}
