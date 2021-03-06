package com.acmed.his.pojo.mo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * Created by Darren on 2018-01-18
 **/
@Data
public class DispensQueryMo {

    @ApiModelProperty("患者姓名或者编号")
    private String name;

    @ApiModelProperty("1:未收费、2:未发药、3：已退款、4：已完成")
    private String status;

    @ApiModelProperty("就诊开始日期")
    private String diagnoseStartDate;

    @ApiModelProperty("就诊结束时间")
    private String diagnoseEndDate;
}
