package com.acmed.his.model;

import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * AccompanyingPrice
 *
 * @author jimson
 * @date 2017/12/22
 */
@Data
@Table(name = "accompanying_price")
@NameStyle
public class AccompanyingPrice {
    /**
     * 医院id 作为主键
     */
    @Id
    private Integer orgCode;
    /**
     * 普通
     */
    private BigDecimal gradeOnePrice;
    /**
     * 专家
     */
    private BigDecimal gradeTwoPrice;
    /**
     * 知名专家
     */
    private BigDecimal gradeThreePrice;
    /**
     * 陪诊费
     */
    private BigDecimal accompanyingPrice;
}
