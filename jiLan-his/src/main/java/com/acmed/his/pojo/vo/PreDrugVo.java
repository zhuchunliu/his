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

        @ApiModelProperty("生产厂家名称")
        private String manufacturerName;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("大单位数量")
        private String numName;

        @ApiModelProperty("频率")
        private String frequencyName;

        @ApiModelProperty("单次剂量")
        private Double singleDose;

        @ApiModelProperty("剂量单位名称")
        private String doseUnitName;

        @ApiModelProperty("单次剂量单位名称")
        private String singleDoseUnitName;

        @ApiModelProperty("备注")
        private String memo;

        @ApiModelProperty("总价")
        private Double totalFee;

    }
}
