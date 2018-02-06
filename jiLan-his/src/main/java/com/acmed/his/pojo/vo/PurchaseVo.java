package com.acmed.his.pojo.vo;

import com.acmed.his.pojo.mo.PurchaseMo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-02-06
 **/
@Data
public class PurchaseVo {
    @ApiModelProperty("主键 null：新增;not null：编辑")
    private String id;

    @ApiModelProperty("供应商")
    private Integer supplierId;

    @ApiModelProperty("供应商名称")
    private String supplierName;

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

    private List<PurchaseVo.PurchaseVoDetail> detailList;


    @Data
    public static class PurchaseVoDetail{
        @ApiModelProperty("主键 null：新增;not null：编辑")
        private String id;

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("药品名称")
        private String name;

        @ApiModelProperty("商品名称")
        private String goodsName;

        @ApiModelProperty("药品规格")
        private String spec;

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
