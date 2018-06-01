package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * FZWOrderMo
 *
 * @author jimson
 * @date 2018/5/18
 */
@Data
public class FZWOrderMo {
    @ApiModelProperty("id 新增的时候不填  修改的时候必填")
    private String id;
    @ApiModelProperty("姓名")
    private String name;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("邮箱")
    private String email;
    @ApiModelProperty("最近的体检时间")
    private String examDate;
    @ApiModelProperty("手机号")
    private String mobile;
    @ApiModelProperty("服务包id")
    private Integer fZWServicePackageId;
    @ApiModelProperty("医生")
    private String doctor;
    @ApiModelProperty("医院")
    private String hospital;
    @ApiModelProperty("备注")
    private String remark;
}
