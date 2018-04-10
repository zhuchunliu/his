package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-03-15
 **/
@Data
public class DrugStockWarnVo {

    @ApiModelProperty("有效期")
    private String expiryDate;

    @ApiModelProperty("入库批号")
    private String batchNumber;

    @ApiModelProperty("数量")
    private String numName;
}
