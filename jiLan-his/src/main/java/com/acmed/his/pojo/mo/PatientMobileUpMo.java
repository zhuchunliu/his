package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PatientMobileUpMo
 *
 * @author jimson
 * @date 2018/1/24
 */
@Data
public class PatientMobileUpMo {
    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("职业")
    private String prof;

    @ApiModelProperty("社保卡")
    private String socialCard;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("真实姓名")
    private String realName;

    @ApiModelProperty("身份证")
    private String idCard;
}
