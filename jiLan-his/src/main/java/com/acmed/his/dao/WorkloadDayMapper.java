package com.acmed.his.dao;

import com.acmed.his.model.WorkloadDay;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-09
 **/
public interface WorkloadDayMapper extends TkMapper<WorkloadDay> {
    void statisWorkload(@Param("orgCode") Integer orgCode,
                        @Param("startTime") String startTime,
                        @Param("endTime") String endTime);

    List<WorkloadDay> getWorkloadList(@Param("orgCode") Integer orgCode,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime);
}
