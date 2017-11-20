package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Dept
 * 科室
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_dept")
@NameStyle
public class Dept {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("机构code")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private String dept;

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("创建时间")
    private Date createAt;

    @ApiModelProperty("修改时间")
    private Date modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;


}
