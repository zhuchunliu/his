package com.acmed.his.model.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-02-06
 **/
@Data
public class FeeItemDto {
    @ApiModelProperty("诊疗项目id")
    private Integer id;

    @ApiModelProperty("医疗机构编码")
    private Integer orgCode;

    @ApiModelProperty("费用类别:使用字典")
    private String feeCategory;

    @ApiModelProperty("费用类别大项：字典表FeeItem")
    private String feeCategoryName;

    @ApiModelProperty("具体费用类型，feeCategory关联的子类型")
    private String category;

    @ApiModelProperty("费用大项目对应子项")
    private String categoryName;

    @ApiModelProperty("项目价格")
    private Float itemPrice;

    @ApiModelProperty("是否有效 0:否;1：是")
    private String isValid;

    @ApiModelProperty("药品备注")
    private String memo;

    @ApiModelProperty("注册时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("创建人")
    private String createUserName;

    @ApiModelProperty("修改人")
    private String modifyBy;
}
