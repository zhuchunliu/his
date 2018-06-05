package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * AddHisApplicationFormMo
 *
 * @author jimson
 * @date 2018/6/4
 */
@Data
public class AddHisApplicationFormMo {
    @ApiModelProperty("联系方式")
    private String mobile;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("联系人")
    private String linkman;

    @ApiModelProperty("医院")
    private String hospital;
}
