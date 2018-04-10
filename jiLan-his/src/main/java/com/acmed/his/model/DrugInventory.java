package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-03-13
 **/
@Data
@Table(name = "t_b_drug_inventory")
@NameStyle
public class DrugInventory {
    @Id
    @ApiModelProperty("库存盘点id")
    private String id;

    @ApiModelProperty("盘点批号")
    private String inventoryNo;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("提交人id")
    private Integer userId;

    @ApiModelProperty("制单日期")
    private String date;

    @ApiModelProperty("审核人员")
    private Integer auditUserId;

    @ApiModelProperty("审核日期")
    private String auditDate;

    @ApiModelProperty("审核标记；0未提交, 1：待审核, 2已审核 , 3:已驳回")
    private Integer status;

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
