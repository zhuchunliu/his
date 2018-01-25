package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * OrgPatientVo
 * 患者就诊过的医院
 * @author jimson
 * @date 2018/1/25
 */
@Data
public class OrgPatientVo {
    @ApiModelProperty("机构code")
    private Integer orgCode;

    @ApiModelProperty("机构名称")
    private String orgName;

    @ApiModelProperty("挂号量")
    private Integer applyNum;

    @ApiModelProperty("医院图片")
    private String imgUrl;

    @ApiModelProperty("医院简介")
    private String introduction;
}
