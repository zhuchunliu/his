package com.acmed.his.pojo.zy;

import com.acmed.his.util.DateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Darren on 2018-06-04
 **/
@Data
public class ZYReceiveQueryMo {
    @ApiModelProperty("订单号,快递号")
    private String name;

    @ApiModelProperty("0:未收货，1：已经收货，默认0")
    private Integer isRecepit = 0;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;

    public String getStartTime() {
        if(StringUtils.isNotEmpty(startTime)){
            return DateTimeUtil.getBeginDate(startTime);
        }
        return startTime;
    }

    public String getEndTime() {
        if(StringUtils.isNotEmpty(endTime)){
            return DateTimeUtil.getEndDate(endTime);
        }
        return endTime;
    }
}
