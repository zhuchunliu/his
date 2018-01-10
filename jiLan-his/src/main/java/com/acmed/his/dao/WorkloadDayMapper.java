package com.acmed.his.dao;

import com.acmed.his.model.WorkloadDay;
import com.acmed.his.model.dto.DoctorApplyNumDto;
import com.acmed.his.model.dto.WorkloadDayAndTotalDto;
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
                                      @Param("userName") String userName,
                                      @Param("startTime") String startTime,
                                      @Param("endTime") String endTime,
                                      @Param("type") Integer type);
    /**
     * 获取某天的预约人数和总预约收入
     * @param orgCode 机构编号
     * @param date 时间
     * @return WorkloadDayAndTotalDto
     */
    WorkloadDayAndTotalDto getWorkloadDayAndTotal(@Param("orgCode") Integer orgCode,
                                                  @Param("date") String date);

    /**
     * 预约量统计
     * @param orgCode
     * @param startTime
     * @param endTime
     * @return
     */
    List<WorkloadDay> getWorkloadListGroupByDate(@Param("orgCode") Integer orgCode,
                                                 @Param("startTime") String startTime,
                                                 @Param("endTime") String endTime);

    List<DoctorApplyNumDto> doctorApplyNum(@Param("orgCode") Integer orgCode);
}
