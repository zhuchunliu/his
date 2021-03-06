package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 处方模板
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_prescription_tpl")
@NameStyle
public class PrescriptionTpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("处方模板id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("模板名称")
    private String tplName;

    @ApiModelProperty("拼音码")
    private String pinYin;

    @ApiModelProperty("处方模板类型 1:药品处方，2：检查处方")
    private String category;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("是否有效 0:无；1：有")
    private String isValid;

    @ApiModelProperty("是否公开 0:否；1：是")
    private String isPublic;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("注册时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;
}
