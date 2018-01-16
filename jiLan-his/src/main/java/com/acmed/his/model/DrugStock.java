package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-15
 **/
@Data
@Table(name = "t_b_drug_stock")
@NameStyle
public class DrugStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty("药品库存id")
    private Integer id;

    @ApiModelProperty("药品code")
    private String drugCode;

    @ApiModelProperty("机构编码")
    private Integer orgCode;

    @ApiModelProperty("有效期")
    private String expiryDate;

    @ApiModelProperty("入库批号")
    private String batchNumber;

    @ApiModelProperty("库存数量")
    private Double num;

    @ApiModelProperty("锁定的库存数量")
    private Double lockNum;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建用户")
    private String createBy;

    @ApiModelProperty("修改用户")
    private String modifyBy;
}
