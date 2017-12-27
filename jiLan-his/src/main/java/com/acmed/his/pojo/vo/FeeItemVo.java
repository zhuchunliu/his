package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-12-01
 **/
@Data
public class FeeItemVo {
    @ApiModelProperty("诊疗项目id")
    private Integer id;

    @ApiModelProperty("费用类别大项：字典表FeeItem")
    private String feeCategory;

    @ApiModelProperty("费用类别大项：字典表FeeItem")
    private String feeCategoryName;

    @ApiModelProperty("费用大项目对应子项")
    private String category;

    @ApiModelProperty("费用大项目对应子项")
    private String categoryName;

    @ApiModelProperty("项目价格")
    private Float itemPrice;

    @ApiModelProperty("药品备注")
    private String memo;
}
