package com.acmed.his.pojo.vo;

import com.acmed.his.model.dto.InspectDayDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by Darren on 2018-01-11
 **/
@Data
public class InspectDayVo {
    @ApiModelProperty("总费用")
    private Double totalFee;

    @ApiModelProperty("日收入")
    private List<DayFee> dayFeeList;

    @ApiModelProperty("检查项")
    private List<InspectDayDto> inspectList;

    @Data
    public class DayFee{
        @ApiModelProperty("日期")
        private Object date;

        @ApiModelProperty("收入")
        private Object fee;
    }
}
