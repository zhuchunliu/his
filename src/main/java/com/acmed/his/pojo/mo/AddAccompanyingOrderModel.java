package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * AccompanyingOrder
 *
 * @author jimson
 * @date 2017/12/22
 */
@Data
public class AddAccompanyingOrderModel {
    /**
     * 真实姓名
     */
    @ApiModelProperty("真实姓名")
    private String realName;
    /**
     * 手机号
     */
    @ApiModelProperty("手机号")
    private String mobile;
    /**
     * 证件类型 1身份证  2港澳通行证  3护照
     */
    @ApiModelProperty("证件类型 1身份证  2港澳通行证  3护照")
    private Integer cardType;
    /**
     * 证件号
     */
    @ApiModelProperty("证件号")
    private String cardNo;
    /**
     * 医保卡卡号
     */
    @ApiModelProperty("医保卡卡号")
    private String medicareCard;
    /**
     * 科室id
     */
    @ApiModelProperty("科室id")
    private Integer deptId;
    /**
     * 预约开始时间
     */
    @ApiModelProperty("预约开始时间")
    private String startTime;
    /**
     * 预约结束时间
     */
    @ApiModelProperty("预约结束时间")
    private String endTime;
    /**
     * 备注
     */
    @ApiModelProperty("备注")
    private String remark;
    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String caseDetail;
    /**
     * 描述图片','拼接
     */
    @ApiModelProperty("描述图片','拼接")
    private String casePictures;
    /**
     * 1  普通   2专家  3特别专家
     */
    @ApiModelProperty("1  普通   2专家  3特别专家")
    private Integer level;
    /**
     * 邀请码
     */
    @ApiModelProperty("邀请码")
    private String invitationCode;
}
