package com.acmed.his.pojo.mo;

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

    @ApiModelProperty("审核标记；0未提交[直接保存], 1：待审核[保存并提交审核] , 2:已审核[直接入库用]")
    private Integer status;

    private List<PurchaseDetail> detailList;

    @Data
    public static class PurchaseDetail{
        @ApiModelProperty("主键 null：新增;not null：编辑")
        private String id;

        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("采购数量")
        private Integer num;

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
