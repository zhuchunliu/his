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

    @ApiModelProperty("机构code")
    private Integer orgCode;

    @ApiModelProperty("科室")
    private String dept;
}
