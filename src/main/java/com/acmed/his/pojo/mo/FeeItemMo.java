package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-22
 **/
@Data
public class FeeItemMo {
    @ApiModelProperty("诊疗项目id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("费用类别:使用字典")
    private String feeCategory;

    @ApiModelProperty("项目价格")
    private Float itemPrice;

    @ApiModelProperty("药品备注")
    private String memo;
}
