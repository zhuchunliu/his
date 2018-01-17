package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-17
 **/
@Data
public class DrugQueryMo {
    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("药品分类 0:西药；1：中成药；2：中药；3：血液制品；4：诊断试剂")
    private String category;
}
