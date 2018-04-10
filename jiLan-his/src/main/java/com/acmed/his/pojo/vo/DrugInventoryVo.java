package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Id;
import java.util.List;

/**
 * Created by Darren on 2018-03-13
 **/
@Data
public class DrugInventoryVo {

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("商品名称")
    private String goodsName;

    @ApiModelProperty("生产厂家名称")
    private String manufacturerName;

    @ApiModelProperty("药品规格")
    private String spec;

    @ApiModelProperty("大单位数量")
    private Integer num;

    @ApiModelProperty("小单位数量")
    private Integer minNum;

    @ApiModelProperty("换算量")
    private Integer conversion;

    @ApiModelProperty("剂量单位库存数量")
    private Double doseNum;

    @ApiModelProperty("单位名称")
    private String unitName;

    @ApiModelProperty("小单位名称")
    private String minUnitName;

    @ApiModelProperty("剂量单位名称")
    private String doseUnitName;

    @ApiModelProperty("二级零售价对应单位  1：小单位minUnitName，2：剂量单位doseUnitName")
    private Integer minPriceUnitType;

    private List<DrugInventoryItemVo> detailList;

    @Data
    public static class DrugInventoryItemVo{

        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("库存详情id")
        private Integer stockId;

        @ApiModelProperty("实际大单位库存数量")
        private Integer actualNum;

        @ApiModelProperty("实际小单位库存数量")
        private Integer actualMinNum;

        @ApiModelProperty("实际剂量单位库存数量")
        private Double actualDoseNum;

        @ApiModelProperty("大单位库存数量")
        private Integer num;

        @ApiModelProperty("小单位库存数量")
        private Integer minNum;

        @ApiModelProperty("剂量单位库存数量")
        private Double doseNum;

        @ApiModelProperty("有效期")
        private String expiryDate;

        @ApiModelProperty("入库批号")
        private String batchNumber;
    }
}
