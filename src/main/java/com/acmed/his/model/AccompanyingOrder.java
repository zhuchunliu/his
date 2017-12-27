package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;


/**
 * AccompanyingOrder
 *
 * @author jimson
 * @date 2017/10/31
 */
@Data
@Table(name = "accompanying_order")
@NameStyle
public class AccompanyingOrder{
    /**
     * 订单号
     */
    @Id
    private String orderCode;
    /**
     * 状态0 未支付  1 已支付
     */
    private Integer status;
    /**
     * 支付状态 0未支付  1已支付
     */
    private Integer payStatus;
    /**
     * 支付方式   1 微信支付
     */
    private Integer payType;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 证件类型 1身份证  2港澳通行证  3护照
     */
    private Integer cardType;
    /**
     * 证件号
     */
    private String cardNo;
    /**
     * 医保卡卡号
     */
    private String medicareCard;
    /**
     * 科室id
     */
    private Integer deptId;
    /**
     * 科室名
     */
    private String dept;
    /**
     * 医院id
     */
    private Integer orgCode;
    /**
     * 医院名
     */
    private String orgName;
    /**
     * 城市id
     */
    private Integer cityId;
    /**
     * 城市名
     */
    private String cityName;
    /**
     * 预约开始时间
     */
    private String startTime;
    /**
     * 预约结束时间
     */
    private String endTime;
    /**
     * 备注
     */
    private String remark;
    private BigDecimal totalBalance;
    private BigDecimal returnBalance;
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
    /**
     * 描述
     */
    private String caseDetail;
    /**
     * 描述图片','拼接
     */
    private String casePictures;

    private Integer delFlag;
    /**
     * 邀请码
     */
    private String otherShareCode;
    /**
     * 评价
     */
    private Double point;
    /**
     * 1  普通   2专家  3特别专家
     */
    private Integer level;
    /**
     * 是否需要陪诊
     * 0 不需要
     * 1需要
     */
    private Integer isAccompanying;
    /**
     * 陪诊费
     */
    private BigDecimal accompanyingPrice;
    /**
     * 服务费  两位小数
     */
    private BigDecimal serviceCharge;
    private String createAt;
    private String modifyAt;
    private String createBy;
    private String modifyBy;
    @ApiModelProperty("邀请码")
    private String invitationCode;
}
