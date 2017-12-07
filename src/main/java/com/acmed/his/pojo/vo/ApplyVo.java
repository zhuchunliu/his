package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-07
 **/
@Data
public class ApplyVo {

    @ApiModelProperty("挂号单id")
    private Integer id;

    @ApiModelProperty("挂号单号")
    private String clinicNo;

    @ApiModelProperty("状态 0:未就诊;1:已就诊")
    private String status;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("机构名")
    private String orgName;

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("挂号费")
    private Double fee;


}
