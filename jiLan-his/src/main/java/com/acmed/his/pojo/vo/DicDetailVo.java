package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DicDetailVo
 *
 * @author jimson
 * @date 2017/11/30
 */
@Data
public class DicDetailVo {
    @ApiModelProperty("字典类型编码")
    private String dicTypeCode;
    @ApiModelProperty("字典项编码")
    private String dicItemCode;
    @ApiModelProperty("字典项名称")
    private String dicItemName;
    @ApiModelProperty("字典类型名称")
    private String dicTypeName;
    @ApiModelProperty("产品编码")
    private String productCode;
}
