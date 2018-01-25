package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * DicItemRemoveMo
 *
 * @author jimson
 * @date 2018/1/25
 */
@Data
public class DicItemRemoveMo {
    @ApiModelProperty("字典code")
    private String dicItemCode;

    @ApiModelProperty("字典类型编码")
    private String dicTypeCode;
}
