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

    @ApiModelProperty("排序模式 1价格优先,3距离优先，默认价格优先")
    private Integer type = 1;

    @ApiModelProperty("经度")
    private String lat;

    @ApiModelProperty("纬度")
    private String lng;


}
