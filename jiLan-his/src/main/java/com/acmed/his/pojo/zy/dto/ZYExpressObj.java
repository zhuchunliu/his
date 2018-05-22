package com.acmed.his.pojo.zy.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-05-22
 **/
@Data
public class ZYExpressObj {

    @ApiModelProperty("快递id")
    private String expId;

    @ApiModelProperty("快递费用")
    private String fee;

    @ApiModelProperty("快递名称")
    private String expName;

    @ApiModelProperty("快递客服电话")
    private String mobile;

    @ApiModelProperty("是否默认")
    private String isDefault;
}
