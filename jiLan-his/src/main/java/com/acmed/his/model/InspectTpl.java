package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 检查模板
 * Created by Darren on 2017-12-01
 **/
@Data
@Table(name = "t_b_inspect_tpl")
@NameStyle
public class InspectTpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("详情id")
    private Integer id;

    @ApiModelProperty("处方id")
    private Integer tplId;

    @ApiModelProperty("检查目的")
    private String aim;

    @ApiModelProperty("检查部位")
    private String part;

    @ApiModelProperty("检查类型 对应字典表：InspectCategory")
    private String category;

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("序号")
    private Integer sn;
}
