package com.acmed.his.model.dto;

import com.acmed.his.model.Org;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;

/**
 * OrgDto
 *
 * @author jimson
 * @date 2018/3/6
 */
@Data
public class OrgDto extends Org{
    @ApiModelProperty("等级")
    private String levelStr;
    @ApiModelProperty("类型")
    private String categoryStr;
    @ApiModelProperty("登录名")
    private String loginName;
    @ApiModelProperty("省")
    private String provinceStr;
    @ApiModelProperty("市")
    private String cityStr;
    @ApiModelProperty("区")
    private String countryStr;

    /**
     * 普通
     */
    @ApiModelProperty("普通")
    private BigDecimal gradeOnePrice;
    /**
     * 专家
     */
    @ApiModelProperty("专家")
    private BigDecimal gradeTwoPrice;
    /**
     * 知名专家
     */
    @ApiModelProperty("知名专家")
    private BigDecimal gradeThreePrice;

    /**
     * 知名专家
     */
    @ApiModelProperty("知名专家")
    private BigDecimal gradeFourPrice;
    /**
     * 陪诊费
     */
    @ApiModelProperty("陪诊费")
    private BigDecimal accompanyingPrice;
}
