package com.acmed.his.model.fzw;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * FZWOrder
 *
 * @author jimson
 * @date 2018/5/17
 */
@Data
@Table(name = "tb_fzw_order")
@NameStyle
public class FZWOrder {
    @Id
    private String id;
    private String orderNum;
    private String name;
    private String sex;
    private String email;
    @ApiModelProperty("最近的体检时间")
    private String examDate;
    private String mobile;
    @ApiModelProperty("服务包id")
    private Integer fZWServicePackageId;
    @ApiModelProperty("服务包str")
    private String fZWServicePackageStr;
    private String doctor;
    private String hospital;
    private String remark;
    // 1未支付   2 已经支付  3 申请退款  4 同意退款  5 完成
    private Integer status;
    // 0 为提交  1 已提交
    private Integer isSend;
    @ApiModelProperty("价格  分")
    private Integer price;
    private String createAt;
    private String modifyAt;
    private String modifyBy;
    private String createBy;
    /**
     * 支付方式  字典表  PaymentMethod
     */
    private String feeType;
    /**
     * 退款单号
     */
    private String returnOrderCode;
    /**
     * 第三方退款单号
     */
    private String otherReturnOrderCode;
    /**
     * 第三方订单号
     */
    private String otherOrderCode;
}
