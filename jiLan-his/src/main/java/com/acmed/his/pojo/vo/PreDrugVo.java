package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-03-02
 **/
@Data
public class PreDrugVo {

    @ApiModelProperty("药品列表")
    public List<PreDrugChild> itemChildList;

    @Data
    public static class PreDrugChild {

        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("频率")
        private String frequency;

        @ApiModelProperty("频率")
        private String frequencyName;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("单价")
        private Double fee;

        @ApiModelProperty("备注")
        private String memo;

        @ApiModelProperty("药品编码")
        private String drugCode;

    }
}
