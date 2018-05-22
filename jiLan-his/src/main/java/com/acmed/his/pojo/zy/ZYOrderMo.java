package com.acmed.his.pojo.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 下单信息
 *
 * Created by Darren on 2018-05-22
 **/
@Data
public class ZYOrderMo {

    @ApiModelProperty("配送方式")
    private String deliverId;

    @ApiModelProperty("药品主键，逗号间隔")
    private List<String> drugIds;


}
