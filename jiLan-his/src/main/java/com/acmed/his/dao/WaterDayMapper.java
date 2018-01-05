package com.acmed.his.dao;

import com.acmed.his.model.WaterDay;
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
     * 统计用 的
     * @param date 日期
     * @return List<WaterDay>
     */
    List<WaterDay> getDailyDataByDate(@Param("date")String date);

    /**
     * 获取某天的数据
     * @param date 日期
     * @return List<WaterDay>
     */
    List<WaterDay> getByDate(@Param("date")String date);
}
