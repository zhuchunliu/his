package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DicItemUpMo
 *
 * @author jimson
 * @date 2018/1/25
 */
@Data
public class DicItemUpMo {
    @ApiModelProperty("字典code")
    private String dicItemCode;

    @ApiModelProperty("字典类型编码")
    private String dicTypeCode;

    @ApiModelProperty("字典项名称")
    private String dicItemName;
}
