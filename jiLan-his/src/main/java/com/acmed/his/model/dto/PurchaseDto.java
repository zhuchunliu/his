package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-03
 **/
@Data
public class PurchaseDto {
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("采购编号")
    private String purchaseNo;

    @ApiModelProperty("采购人")
    private String userName;

    @ApiModelProperty("采购日期")
    private String date;

    @ApiModelProperty("采购总额")
    private Double bidFee;

    @ApiModelProperty("审核人")
    private String auditUserName;

    @ApiModelProperty("审核日期")
    private String auditDate;

    @ApiModelProperty("供应商")
    private String supplierName;

    @ApiModelProperty("审核标记；0未提交, 1：待审核, 2已审核 , 3:已驳回")
    private Integer status;
}
