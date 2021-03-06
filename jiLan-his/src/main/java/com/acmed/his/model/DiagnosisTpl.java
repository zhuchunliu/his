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
    @ApiModelProperty("诊断模板id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("模板名称")
    private String tplName;

    @ApiModelProperty("诊断分类:字典中获取")
    private String category;

    @ApiModelProperty("诊断")
    private String diagnosis;

    @ApiModelProperty("是否有效 0:无；1：有")
    private String isValid;

    @ApiModelProperty("是否公开 0:否；1：是")
    private String isPublic;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("药品备注")
    private String memo;

    @ApiModelProperty("注册时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;
}
