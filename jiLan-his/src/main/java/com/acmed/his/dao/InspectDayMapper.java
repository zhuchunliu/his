package com.acmed.his.dao;

import com.acmed.his.model.InspectDay;
import com.acmed.his.model.dto.InspectDayDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-05
 **/
public interface InspectDayMapper extends TkMapper<InspectDay>{
    void staticInspect(@Param("orgCode") Integer orgCode,
                       @Param("startTime") String startTime,
                       @Param("endTime") String endTime);

    List<InspectDayDto> getInspectList(@Param("orgCode") Integer orgCode,
                                       @Param("categoryType") String categoryType,
                                       @Param("startTime") String startTime,
                                       @Param("endTime") String endTime);
}
