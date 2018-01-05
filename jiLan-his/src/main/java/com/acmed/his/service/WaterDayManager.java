package com.acmed.his.service;

import com.acmed.his.dao.WaterDayMapper;
import com.acmed.his.model.WaterDay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * WaterDayManager
 *
 * @author jimson
 * @date 2018/1/5
 */
@Service
@Transactional
public class WaterDayManager {
    @Autowired
    private WaterDayMapper waterDayMapper;


    // TODO 周月季度年报表查询还没完成

    /**
     * 跑批
     * @param date 日期 建议当天时间减一
     */
    public void paopi(String date){
        List<WaterDay> byDate = waterDayMapper.getByDate(date);
        if (byDate.size()==0){
            List<WaterDay> dailyDataByDate = waterDayMapper.getDailyDataByDate(date);
            waterDayMapper.insertList(dailyDataByDate);
        }
    }
}
