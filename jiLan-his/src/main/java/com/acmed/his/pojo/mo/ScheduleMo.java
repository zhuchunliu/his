package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Darren on 2017-12-21
 **/
@Data
public class ScheduleMo {
    @ApiModelProperty("用户主键表")
    private Integer userid;

    @ApiModelProperty("排班日期")
    private Date date;

    @ApiModelProperty("排班类型")
    private String type;
}
