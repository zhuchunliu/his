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

    @ApiModelProperty("费用类别大项：字典表FeeItem")
    private String feeCategory;

    @ApiModelProperty("费用大项目对应子项")
    private String category;

    @ApiModelProperty("项目价格")
    private Float itemPrice;

    @ApiModelProperty("药品备注")
    private String memo;


}
