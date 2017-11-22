package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 检查处方
 * Created by Darren on 2017-11-22
 **/
@Data
public class PreInspectMo {
    @ApiModelProperty
    private Integer id;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("药品集合")
    private List<PreInspectMo.Inspect> inspectList;

    @ApiModelProperty("附加费")
    private List<PreInspectMo.Charge> chargeList;

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

    }

    @Data
    public class Charge{
        @ApiModelProperty("费用类型")
        private String category;

        @ApiModelProperty("费用")
        private Double fee;
    }
}
