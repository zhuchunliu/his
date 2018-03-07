package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-07
 **/
@Data
public class DispensingRefundApplyMo {
    @ApiModelProperty("挂号id")
    private String applyId;

    @ApiModelProperty("付费类型")
    private String feeType;

    @ApiModelProperty("退款理由")
    private String reason;

    @ApiModelProperty("退款说明")
    private String state;
}
