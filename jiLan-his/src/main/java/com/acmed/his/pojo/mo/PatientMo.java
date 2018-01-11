package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * PatientMo
 *
 * @author jimson
 * @date 2018/1/11
 */
@Data
public class PatientMo {

    @ApiModelProperty("用户姓名")
    private String userName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;


    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("号码")
    private String mobile;

    @ApiModelProperty("体重")
    private BigDecimal weight;

    @ApiModelProperty("职业")
    private String prof;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("社保卡")
    private String socialCard;

    @ApiModelProperty("过敏史")
    private String anaphylaxis;
}
