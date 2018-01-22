package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SupplyVsOrg
 * 渠道机构表
 * @author jimson
 * @date 2018/1/22
 */
@Data
@Table(name = "t_b_supply_vs_org")
@NameStyle
public class SupplyVsOrg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("主键")
    private Integer id;

    @ApiModelProperty("机构id")
    private Integer orgCode;

    @ApiModelProperty("渠道id")
    private Integer supplyId;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;

    @ApiModelProperty("是否删除")
    private String removed;
}
