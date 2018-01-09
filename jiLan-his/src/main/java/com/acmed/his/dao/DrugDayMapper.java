package com.acmed.his.dao;

import com.acmed.his.model.DrugDay;
import com.acmed.his.model.dto.DrugDayDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-05
 **/
public interface DrugDayMapper extends TkMapper<DrugDay>{
    void staticSale(@Param("orgCode") Integer orgCode,
                    @Param("startTime") String startTime,
                    @Param("endTime") String endTime);

    List<DrugDayDto> getDrugDayList(@Param("orgCode") Integer orgCode,
                                    @Param("startTime") String startTime,
                                    @Param("endTime") String endTime);

    Integer getDrugDayTotal(@Param("orgCode") Integer orgCode,
                        @Param("startTime") String startTime,
                        @Param("endTime") String endTime);


}
