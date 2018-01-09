package com.acmed.his.dao;

import com.acmed.his.model.Inspect;
import com.acmed.his.model.dto.InspectDayDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface InspectMapper extends TkMapper<Inspect> {
    void delByPreId(@Param("prescriptionId") String prescriptionId);

    List<InspectDayDto> getItemList(@Param("orgCode") Integer orgCode,
                     @Param("categoryType") String categoryType,
                     @Param("startTime") String startTime,
                     @Param("endTime") String endTime);

    Integer getItemTotal(@Param("orgCode") Integer orgCode,
                                     @Param("startTime") String startTime,
                                     @Param("endTime") String endTime);
}
