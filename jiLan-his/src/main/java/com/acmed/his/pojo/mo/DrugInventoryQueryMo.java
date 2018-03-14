package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-13
 **/
@Data
public class DrugInventoryQueryMo {
    @ApiModelProperty("采购单号")
    private String inventoryNo;
    @ApiModelProperty("审核状态 0未审核, 1：待审核, 2已审核 , 3:已驳回")
    private Integer status;
    @ApiModelProperty("开始时间")
    private String startTime;
    @ApiModelProperty("结束时间")
    private String endTime;
}
