package com.acmed.his.pojo.vo;

import com.acmed.his.pojo.mo.PrescriptionTplMo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * Created by Darren on 2017-12-01
 **/
@Data
public class PrescriptionTplVo {
    @ApiModelProperty("模板id")
    private Integer id;

    @ApiModelProperty("模板名称")
    private String tplName;

    @ApiModelProperty("模板类型")
    private String category;

    @ApiModelProperty("模板类型名称")
    private String categoryName;

    @ApiModelProperty("诊断信息")
    private String diagnosis;

    @ApiModelProperty("医嘱")
    private String advice;

    @ApiModelProperty("描述")
    private String description;

    @ApiModelProperty("模板明细")
    private List<PrescriptionTplMo.Item> itemList;

    @ApiModelProperty("模板明细")
    private List<PrescriptionTplMo.InspectDetail> inspectList;

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

    @Data
    public class InspectDetail{
        @ApiModelProperty("检查目的")
        private String aim;

        @ApiModelProperty("检查部位")
        private String part;

        @ApiModelProperty("检查类型")
        private String category;

        @ApiModelProperty("备注")
        private String memo;
    }
}