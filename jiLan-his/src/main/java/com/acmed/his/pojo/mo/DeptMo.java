package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class DeptMo {
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("科室名称")
    private String dept;

    @ApiModelProperty("不传默认是当前医院")
    private Integer orgCode;

    @ApiModelProperty("是否是优势科室   0不是  1 是")
    private Integer superiorityFlag;
}
