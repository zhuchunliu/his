package com.acmed.his.dao;

import com.acmed.his.model.Schedule;
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
     * @param userId
     * @param startTime
     * @param endTime
     * @return
     */
    List<ScheduleDto> getScheduleList(@Param("orgCode") Integer orgCode, @Param("deptId") Integer deptId,
                                      @Param("userId") Integer userId, @Param("startTime") String startTime,
                                      @Param("endTime") String endTime);

    /**
     * 获取上周排班列表
     *
     * @param startTime
     * @param endTime
     * @param userIds
     * @return
     */
    List<ScheduleDto> getPreScheduleList(@Param("startTime") String startTime, @Param("endTime") String endTime,
                                      @Param("userIds") String[] userIds);
}
