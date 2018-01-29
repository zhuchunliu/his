package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-01-23
 **/
@Data
public class InspectTplMo {
    @ApiModelProperty("处方模板id")
    private Integer tplId;

    private List<InspectMo> list;

    @Data
    public static class InspectMo{
        @ApiModelProperty("检查目的")
        private String aim;

        @ApiModelProperty("检查部位")
        private String part;

        @ApiModelProperty("检查类型 字典表:InspectCategory")
        private String category;

        @ApiModelProperty("备注")
        private String memo;
    }
}
