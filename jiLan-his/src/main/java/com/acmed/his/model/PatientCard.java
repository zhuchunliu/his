package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PatientCard
 *
 * @author jimson
 * @date 2018/1/24
 */
@Data
@Table(name = "t_b_patient_card")
@NameStyle
public class PatientCard {
    @Id
    @ApiModelProperty("主键")
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

    @ApiModelProperty("删除标记 0未删除 1已经删除")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
