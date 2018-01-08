package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-05
 **/
@Data
@Table(name = "t_r_inspect_day")
@NameStyle
public class InspectDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("检查类型")
    private String category;

    @ApiModelProperty("检查次数")
    private Double num;

    @ApiModelProperty("检查费用")
    private Double fee;

    @ApiModelProperty("报表日期")
    private String date;
}
