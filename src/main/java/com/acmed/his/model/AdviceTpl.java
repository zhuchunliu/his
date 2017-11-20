package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 医嘱模板
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_advice_tpl")
@NameStyle
public class AdviceTpl {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("诊疗项目id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("医嘱")
    private String advice;

    @ApiModelProperty("是否有效 0：无效; 1:有效")
    private String isvalid;
}
