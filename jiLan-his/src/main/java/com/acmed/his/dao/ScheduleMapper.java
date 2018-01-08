package com.acmed.his.dao;

import com.acmed.his.model.Schedule;
import com.acmed.his.model.dto.ScheduleApplyDto;
import com.acmed.his.model.dto.ScheduleDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-12-20
 **/
public interface ScheduleMapper extends TkMapper<Schedule> {
    /**
     * 获取用户最近一次排班
     * @param userid
     * @return
     */
    Schedule getRecentSchedule(@Param("userid") Integer userid);

    /**
     * 根据查询条件获取排班列表
     * @param orgCode
     * @param deptId
     * @param userIds
     * @param startTime
     * @param endTime
     * @return
     */
    List<ScheduleDto> getScheduleList(@Param("orgCode") Integer orgCode, @Param("deptId") Integer deptId,
                                      @Param("userIds") List<String> userIds, @Param("startTime") String startTime,
                                      @Param("endTime") String endTime);

    /**
     * 获取排班信息
     *
     * @param orgCode
     * @param deptId
     * @param startTime
     * @param endTime
     * @return
     */
    List<ScheduleApplyDto> getScheduleApplyList(@Param("orgCode") Integer orgCode, @Param("deptId") Integer deptId,
                                                @Param("startTime") String startTime,@Param("endTime") String endTime);
}
