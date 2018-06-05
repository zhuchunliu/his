package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * UpdateHisApplicationFormStatusMo
 *
 * @author jimson
 * @date 2018/6/4
 */
@Data
public class UpdateHisApplicationFormStatusMo {
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("状态  1待处理  2联系成功  3无效信息")
    private Integer status;

}
