package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-01-03
 **/
@Data
public class PurchaseMo {

    @ApiModelProperty("主键 null：新增;not null：编辑")
    private String id;

    @ApiModelProperty("供应商")
    private Integer supplierId;

    @ApiModelProperty("采购编号")
    private String purchaseNo;

    @ApiModelProperty("采购批发总额")
    private Double bidFee;

    @ApiModelProperty("采购处方总额")
    private Double retailFee;

    @ApiModelProperty("制单日期")
    private String date;

    @ApiModelProperty("审核标记；0未审核：1，已审核")
    private Integer status;

    private List<Detail> detailList;

    @Data
    public class Detail{
        @ApiModelProperty("主键 null：新增;not null：编辑")
        private String id;

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("采购数量")
        private Double num;

        @ApiModelProperty("采购单位")
        private String unit;

        @ApiModelProperty("进价")
        private Double bid;

        @ApiModelProperty("零售价")
        private Double retailPrice;

        @ApiModelProperty("采购批发总额")
        private Double bidFee;

        @ApiModelProperty("采购处方总额")
        private Double retailFee;

        @ApiModelProperty("有效期")
        private String expiryDate;

        @ApiModelProperty("入库批号")
        private String batchNumber;
    }
}
