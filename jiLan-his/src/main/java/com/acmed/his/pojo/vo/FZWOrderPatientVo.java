package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * FZWOrderPatientVo
 *
 * @author jimson
 * @date 2018/5/30
 */
@Data
public class FZWOrderPatientVo {
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("预约单号")
    private String orderNum;
    private String name;
    private String sex;
    @ApiModelProperty("最近的体检时间")
    private String examDate;
    private String mobile;
    @ApiModelProperty("服务包id")
    private Integer fZWServicePackageId;
    @ApiModelProperty("服务包str")
    private String fZWServicePackageStr;
    @ApiModelProperty("医生")
    private String doctor;
    @ApiModelProperty("医院")
    private String hospital;
    @ApiModelProperty("备注")
    private String remark;
    // 1未支付   2 已经支付  3 申请退款  4 同意退款  5 完成
    @ApiModelProperty("1未支付   2 已经支付  3 申请退款  4 同意退款  5 完成")
    private Integer status;
    @ApiModelProperty("价格  分")
    private Integer price;
    @ApiModelProperty("创建时间")
    private String createAt;
}
