package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-04-13
 **/
@Data
public class DrugRetailVo {
    @ApiModelProperty("药品零售主键")
    private String id;

    @ApiModelProperty("总费用")
    private Double fee;

    private List<DrugRetailItemVo> itemList;

    @Data
    public static class DrugRetailItemVo{

        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("备注")
        private String remark;

        @ApiModelProperty("药品名称")
        private String name;

        @ApiModelProperty("商品名称")
        private String goodsName;

        @ApiModelProperty("生产厂家名称")
        private String manufacturerName;

        @ApiModelProperty("药品规格")
        private String spec;

    }

}
