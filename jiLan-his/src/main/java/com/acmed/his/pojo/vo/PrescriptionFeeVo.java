package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-01-22
 **/
@Data
public class PrescriptionFeeVo {
    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("应收款")
    private Double receivables;

    @ApiModelProperty("实收款")
    private Double receipts;

    @ApiModelProperty("已退款")
    private Double refunded;
}
