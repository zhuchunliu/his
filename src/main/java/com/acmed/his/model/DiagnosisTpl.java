package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 诊断模板
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_diagnosis_tpl")
@NameStyle
public class DiagnosisTpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("诊疗项目id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("诊断分类:字典中获取")
    private String category;

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("是否有效 0:无；1：有")
    private String isValid;

    @ApiModelProperty("药品备注")
    private String memo;
}
