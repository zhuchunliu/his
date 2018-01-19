package com.acmed.his.pojo.mo;

import com.acmed.his.util.DateTimeUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Optional;

/**
 * Created by Darren on 2018-01-19
 **/
@Data
public class ReportQueryMo {

    @ApiModelProperty("开始时间")
    String startTime;

    @ApiModelProperty("结束时间")
    String endTime;


    public String getStartTime() {
        return Optional.ofNullable(startTime).map(DateTimeUtil::getBeginDate).orElse(null);
    }

    public String getEndTime() {
        return Optional.ofNullable(endTime).map(DateTimeUtil::getEndDate).orElse(null);
    }

}
