package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2017-11-30
 **/
@Data
public class PreMo {
    @ApiModelProperty
    private Integer id;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("药品集合")
    private List<PreMo.Item> itemList;

    @ApiModelProperty("附加费")
    private List<PreMo.Charge> chargeList;

    @ApiModelProperty("药品集合")
    private List<PreMo.Inspect> inspectList;

    @Data
    public class Item{
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("频率")
        private Integer frequency;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("备注")
        private String memo;
    }

    @Data
    public class Charge{
        @ApiModelProperty("费用类型")
        private String category;
    }

    @Data
    public class Inspect{

        @ApiModelProperty("检查目的")
        private String aim;

        @ApiModelProperty("检查部位")
        private String part;

        @ApiModelProperty("检查类型")
        private String category;

        @ApiModelProperty("病情摘要")
        private String summary;

        @ApiModelProperty("检查诊断")
        private String diagnosis;

        @ApiModelProperty("备注")
        private String memo;

    }

}
