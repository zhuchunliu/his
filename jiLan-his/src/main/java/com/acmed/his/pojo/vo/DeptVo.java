package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-29
 **/
@Data
public class DeptVo {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("科室")
    private String dept;

    @ApiModelProperty("是否是优势科室   0不是  1 是")
    private Integer superiorityFlag;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("创建时间")
    private String createAt;
}
