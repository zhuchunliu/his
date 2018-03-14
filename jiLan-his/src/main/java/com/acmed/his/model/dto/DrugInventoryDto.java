package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-13
 **/
@Data
public class DrugInventoryDto {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("申请编号")
    private String inventoryNo;

    @ApiModelProperty("申请人")
    private String userName;

    @ApiModelProperty("采购日期")
    private String date;

    @ApiModelProperty("审核人")
    private String auditUserName;

    @ApiModelProperty("审核日期")
    private String auditDate;

    @ApiModelProperty("审核标记；0未提交, 1：待审核, 2已审核 , 3:已驳回")
    private Integer status;
}
