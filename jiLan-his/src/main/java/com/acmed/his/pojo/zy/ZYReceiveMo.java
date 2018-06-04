package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-06-04
 **/
@Data
public class ZYReceiveMo {
    @ApiModelProperty("详情id")
    private String itemId;

    @ApiModelProperty("收货数量")
    private Integer receiveNum;
}
