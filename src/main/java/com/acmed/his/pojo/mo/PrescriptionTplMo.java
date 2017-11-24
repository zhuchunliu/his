package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class PrescriptionTplMo {

    @ApiModelProperty("模板id")
    private Integer id;

    @ApiModelProperty("模板名称")
    private String tplName;

    @ApiModelProperty("拼音码")
    private String pinYin;

    @ApiModelProperty("处方模板类型 0：西药处方; 1:中药处方")
    private String category;

    @ApiModelProperty("诊断信息")
    private String diagnosis;

    @ApiModelProperty("医嘱")
    private String advice;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("模板明细")
    private List<Item> itemList;

    @Data
    public class Item {

        @ApiModelProperty("用药id")
        private Integer drugId;

        @ApiModelProperty("药品类型")
        private String drugCategory;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("途径 用法")
        private String way;

        @ApiModelProperty("频率,用药频率")
        private Float frequency;

        @ApiModelProperty("天数")
        private Integer num;

        @ApiModelProperty("疗程")
        private Integer course;

        @ApiModelProperty("备注")
        private String memo;
    }
}
