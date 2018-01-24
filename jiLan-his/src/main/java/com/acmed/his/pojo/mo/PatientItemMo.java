package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PatientItemMo
 *
 * @author jimson
 * @date 2018/1/23
 */
@Data
public class PatientItemMo {
    @ApiModelProperty("患者姓名/手机号 模糊匹配")
    private String vague;

    @ApiModelProperty("机构编码  不传传默认是账号所在机构")
    private Integer orgCode;

    @ApiModelProperty("是否是黑名单  0不是黑名单   1 是黑名单  不传拉全部")
    private Integer blackFlag;
}
