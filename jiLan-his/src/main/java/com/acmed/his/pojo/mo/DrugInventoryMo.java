package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-03-13
 **/
@Data
public class DrugInventoryMo {
    @ApiModelProperty("主键 null：新增;not null：编辑")
    private String id;

    @ApiModelProperty("申请编号")
    private String inventoryNo;

    @ApiModelProperty("制单日期")
    private String date;

    @ApiModelProperty("审核标记；0未提交[直接保存], 1：待审核[保存并提交审核] , 2:已审核[直接入库用]")
    private Integer status;

    private List<DrugInventoryMo.DrugInventoryItemMo> detailList;

    @Data
    public static class DrugInventoryItemMo{
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("库存详情id")
        private Integer stockId;

        @ApiModelProperty("大单位库存数量")
        private Integer num;

        @ApiModelProperty("小单位库存数量")
        private Integer minNum;

        @ApiModelProperty("剂量单位库存数量")
        private Double doseNum;
    }
}
