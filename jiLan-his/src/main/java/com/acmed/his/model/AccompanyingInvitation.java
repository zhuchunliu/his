package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * AccompanyingInvitation
 * 陪诊邀请表
 * @author jimson
 * @date 2017/12/27
 */
@Data
@Table(name = "accompanying_invitation")
@NameStyle
public class AccompanyingInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("id")
    private Integer id;
    @ApiModelProperty("渠道id")
    private Integer userId;
    @ApiModelProperty("患者id")
    private String patientId;
    @ApiModelProperty("邀请码")
    private String invitationCode;
}
