package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-23
 **/
@Data
public class InspectTplVo {

    @ApiModelProperty("检查目的")
    private String aim;

    @ApiModelProperty("检查部位")
    private String part;

    @ApiModelProperty("检查类型 字典表:InspectCategory")
    private String category;

    @ApiModelProperty("检查类型")
    private String categoryName;

    @ApiModelProperty("价格")
    private Double fee;

    @ApiModelProperty("备注")
    private String memo;
}
