package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("药品id")
    private Integer id;

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品名称拼音")
    private String pinYin;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("商品名称拼音")
    private String goodsPinYin;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品")
    private String category;

    @ApiModelProperty("剂型，字典表：DrugForm")
    private String drugForm;

    @ApiModelProperty("生产厂家 对应生产商")
    private String manufacturer;

    @ApiModelProperty("生产厂家 对应生产商")
    private Integer manufacturerId;

    @ApiModelProperty("准字号")
    private String approvalNumber;

    @ApiModelProperty("单位（g/条）大单位 字典表:Unit")
    private String unit;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("药品说明")
    private String instructions;

    @ApiModelProperty("外部id")
    private String outId;

}
