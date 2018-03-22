package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-22
 **/
@Data
public class DrugDictQueryMo {
    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品分类 参考字典表：DrugClassification")
    private String category;
}
