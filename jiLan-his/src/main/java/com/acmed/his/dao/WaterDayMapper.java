package com.acmed.his.dao;

import com.acmed.his.model.WaterDay;
import com.acmed.his.model.dto.WaterDayAndMonthCountDto;
import com.acmed.his.model.dto.WaterDetailDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * WaterDayMapper
 *
 * @author jimson
 * @date 2018/1/5
 */
public interface WaterDayMapper extends TkMapper<WaterDay>{
    /**
     * 从其他表跑批出的报表数据
     * @param date 日期
     * @return List<WaterDay>
     */
    List<WaterDay> getDailyDataByDate(@Param("date")String date);

    /**
     * 获取某天的报表数据
     * @param date 日期
     * @return List<WaterDay>
     */
    List<WaterDay> getByDate(@Param("date")String date);

    /**
     * 查询某段时间的报表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param orgCode 机构编码
     * @return List<WaterDay>
     */
    List<WaterDay> getListBetweenTimes(@Param("startTime")String startTime , @Param("endTime")String endTime, @Param("orgCode") Integer orgCode);

    /**
     * 获取某段时间的收支明细
     * @param orgCode 机构编码
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return List<WaterDay>
     */
    List<WaterDetailDto> waterDetailList(@Param("orgCode")Integer orgCode,@Param("startTime")String startTime,@Param("endTime")String endTime);

    /**
     * 传年  如2017 返回按月分组的报表
     * @param orgCode 机构编码
     * @param date 年
     * @return List<WaterDay>
     */
    List<WaterDay> getListByYearGroupByMonth(@Param("orgCode")Integer orgCode,@Param("date")String date);

    /**
     * 查询所在月的列表
     * @param orgCode 机构
     * @param date 日期
     * @return List<WaterDay>
     */
    List<WaterDay> getListByYearMonth(@Param("orgCode")Integer orgCode,@Param("date")String date);

    /**
     * 获取某天收益和那天所在月的收益
     * @param orgCode 机构
     * @param date 时间
     * @return WaterDayAndMonthCountDto
     */
    WaterDayAndMonthCountDto getWaterDayAndMonthCount(@Param("orgCode")Integer orgCode,@Param("date")String date);
}
