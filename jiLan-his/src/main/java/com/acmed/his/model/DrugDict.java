package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 药品字典
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_drugdict")
@NameStyle
public class DrugDict {
    @Id
    @ApiModelProperty("药品编码")
    private String code;

    @ApiModelProperty("英文名")
    private String englishName;

    @ApiModelProperty("药品规范名")
    private String specName;

    @ApiModelProperty("常用名")
    private String commName;

    @ApiModelProperty("拼音码")
    private String pinYin;

    @ApiModelProperty("ATC编码")
    private String atcCode;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品")
    private String category;

    @ApiModelProperty("药品类型 0:OTC药品; 1:处方药品")
    private String classification;

    @ApiModelProperty("药品说明")
    private String instructions;

    @ApiModelProperty("药品功效")
    private String efficacy;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("计量单位")
    private String unit;

    @ApiModelProperty("包装单位")
    private String packUnit;

}
