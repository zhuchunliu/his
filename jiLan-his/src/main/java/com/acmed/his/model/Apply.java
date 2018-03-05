package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 挂号
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_apply")
@NameStyle
public class Apply {

    @Id
    @ApiModelProperty("挂号单id")
    private String id;

    @ApiModelProperty("挂号单号 医疗机构id+排序")
    private String clinicNo;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("机构名")
    private String orgName;

    @ApiModelProperty("患者id")
    private String patientId;

    @ApiModelProperty("患者庫id")
    private String patientItemId;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("性别 0:男;1:女")
    private String gender;

    @ApiModelProperty("拼音")
    private String pinYin;

    @ApiModelProperty("挂号费")
    private Double fee;

    @ApiModelProperty("是否已付费 0:否; 1:是")
    private String isPaid;

    @ApiModelProperty("付费类型")
    private String feeType;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;

    @ApiModelProperty("过期时间")
    private String expire;

    @ApiModelProperty("状态 0:未就诊;1:已就诊,2:已取消，3就诊中")
    private String status;

    @ApiModelProperty("科室名字")
    private String deptName;

    @ApiModelProperty("科室id")
    private Integer dept;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty("总费用")
    private Double totalFee;

    @ApiModelProperty("医生id")
    private Integer doctorId;

    @ApiModelProperty("医生名字")
    private String doctorName;

    @ApiModelProperty("预约时间")
    private String appointmentTime;

    @ApiModelProperty("是否是初诊   0不是初诊   1  是初诊")
    private Integer isFirst;


}
