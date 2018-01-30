package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PatientCardMo
 *
 * @author jimson
 * @date 2018/1/29
 */
@Data
public class PatientCardMo {
    @ApiModelProperty("主键，新增不填，填了表示修改编辑")
    private String id;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("号码")
    private String mobile;

    @ApiModelProperty("身份证")
    private String idCard;

    @ApiModelProperty("社保卡")
    private String socialCard;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("关系")
    private String relation;
}
