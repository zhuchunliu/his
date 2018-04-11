package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2018-04-10
 **/
@Data
public class DrugZYQueryMo {
    @ApiModelProperty("药品名称")
    private String name;

    @ApiModelProperty("经度")
    private String lat;

    @ApiModelProperty("纬度")
    private String lng;


}
