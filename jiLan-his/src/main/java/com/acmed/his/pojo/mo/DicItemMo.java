package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DicItemMo
 *
 * @author jimson
 * @date 2018/1/23
 */
@Data
public class DicItemMo {
    @ApiModelProperty("字典项名称")
    private String dicItemName;

    @ApiModelProperty("字典类型编码")
    private String dicTypeCode;
}
