package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-04-13
 **/
@Data
public class DrugRetailQueryMo {

    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("支付状态 0:未支付,1:已支付")
    private Integer payStatus;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

}
