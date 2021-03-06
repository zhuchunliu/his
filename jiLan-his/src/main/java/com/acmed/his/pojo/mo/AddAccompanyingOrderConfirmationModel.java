package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * AddAccompanyingOrderConfirmationModel
 *
 * @author jimson
 * @date 2017/12/22
 */
@Data
public class AddAccompanyingOrderConfirmationModel {
    /**
     * 订单号
     */
    @ApiModelProperty("订单号")
    private String orderCode;
    /**
     * 医生名字
     */
    @ApiModelProperty("医生名字")
    private String doctorName;
    /**
     * 陪诊人手机号
     */
    @ApiModelProperty("陪诊人手机号")
    private String mobile;
    /**
     * 陪诊人姓名
     */
    @ApiModelProperty("陪诊人姓名")
    private String accompanyingName;
    /**
     * 科室id
     */
    @ApiModelProperty("科室id")
    private Integer deptId;
    /**
     * 就诊时间
     */
    @ApiModelProperty("就诊时间")
    private String time;
    /**
     * 服务费  两位小数
     */
    @ApiModelProperty("服务费  两位小数")
    private BigDecimal serviceCharge;
    /**
     * 陪诊费
     */
    @ApiModelProperty("陪诊费")
    private BigDecimal accompanyingPrice;
}
