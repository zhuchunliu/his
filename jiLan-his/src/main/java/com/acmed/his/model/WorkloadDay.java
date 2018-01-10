package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-09
 **/
@Data
@Table(name = "t_r_workload_day")
@NameStyle
public class WorkloadDay {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("用户主键")
    private Integer userId;

    @ApiModelProperty("就诊量")
    private Integer diagnoseNum;

    @ApiModelProperty("取消量")
    private Integer cancelNum;

    @ApiModelProperty("违约量")
    private Integer breachNum;

    @ApiModelProperty("挂号金额")
    private Double applyFee;

    @ApiModelProperty("处方金额")
    private Double prescriptionFee;

    @ApiModelProperty("挂号退款金额")
    private Double applyRefundFee;

    @ApiModelProperty("处方退款金额")
    private Double prescriptionRefundFee;

    @ApiModelProperty("报表日期")
    private String date;
}
