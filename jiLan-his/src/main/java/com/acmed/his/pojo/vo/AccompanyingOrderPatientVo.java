package com.acmed.his.pojo.vo;

import lombok.Data;


/**
 * AccompanyingOrderPatientVo
 *
 * @author jimson
 * @date 2017/12/22
 */
@Data
public class AccompanyingOrderPatientVo {
    /**
     * 状态
     */
    private Integer status;
    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 就诊人名字
     */
    private String realName;
    /**
     * 期望预约开始时间
     */
    private String startTime;
    /**
     * 期望预约结束时间
     */
    private String endTime;
    /**
     * 订单金额
     */
    private String price;
    /**
     * 期望医院
     */
    private String hopeHospitalName;

    /**
     * 期望门诊
     */
    private String hopeDepartmentName;
    /**
     * 期望城市
     */
    private String hopeCityName;

    /**
     * 医生名字
     */
    private String doctorName;
    private Integer level;

    /**
     * 医院名
     */
    private String realHospitalName;

    /**
     * 城市名
     */
    private String realCityName;

    /**
     * 科室名
     */
    private String realDepartmentName;


    /**
     * 评价
     */
    private Double point;


    /**
     * 证件类型
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
     * 下单时间
     */
    private String createTime;

    /**
     * 暂时没有了
     * 是否需要陪诊
     * 0 不需要
     * 1需要
     */
    private Integer isAccompanying;

    /**
     * 支付方式   1 微信支付
     */
    private Integer payType;

    /**
     * 就诊时间
     */
    private String time;

    private String remark;

    /**
     * 陪诊人手机号
     */
    private String accompanyingMobile;
    /**
     * 陪诊人姓名
     */
    private String accompanyingName;

    private String mobile;
}
