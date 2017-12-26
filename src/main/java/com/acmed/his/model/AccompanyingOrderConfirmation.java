package com.acmed.his.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * AccompanyingOrderConfirmation
 *
 * @author jimson
 * @date 2017/10/31
 */
@Data
@Table(name = "accompanying_order_confirmation")
@NameStyle
public class AccompanyingOrderConfirmation{
    /**
     * 订单号
     */
    @Id
    private String orderCode;
    /**
     * 医生名字
     */
    private String doctorName;
    /**
     * 陪诊人手机号
     */
    private String mobile;
    /**
     * 陪诊人姓名
     */
    private String accompanyingName;
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
     * 就诊时间
     */
    private String time;
    /**
     * 服务费  两位小数
     */
    private BigDecimal serviceCharge;
    /**
     * 陪诊费
     */
    private BigDecimal accompanyingPrice;
    private String createAt;
    private String modifyAt;
    private String createBy;
    private String modifyBy;

}
