package com.acmed.his.service;

import com.acmed.his.dao.WaterDayMapper;
import com.acmed.his.model.WaterDay;
import com.acmed.his.model.dto.ShouzhiCountDto;
import com.acmed.his.model.dto.WaterDayAndMonthCountDto;
import com.acmed.his.model.dto.WaterDetailDto;
import com.acmed.his.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
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

    /**
     * 定时任务每天1点运行
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void task(){
        paopi(LocalDate.now().minusDays(1).toString());
    }

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

    /**
     * 获取一段时间内的报表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param orgCode 机构编码
     * @return List<WaterDay>
     */
    public List<WaterDay> getListBetweenTimes(String startTime,String endTime,Integer orgCode){
        return waterDayMapper.getListBetweenTimes(startTime,endTime,orgCode);
    }

    /**
     * 获取一段时间内的报表数据的累加
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param orgCode 机构编码
     * @return List<WaterDay>
     */
    public WaterDay getListBetweenTimesSum(String startTime,String endTime,Integer orgCode){
        return waterDayMapper.getListBetweenTimesSum(startTime,endTime,orgCode);
    }

    /**
     * 获取一段时间内的报表
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param orgCode 机构编码
     * @return PageResult<WaterDay>
     */
    public PageResult<WaterDay> getListBetweenTimesByPage(String startTime,String endTime,Integer orgCode,Integer pageNum,Integer pageSize){
        PageResult<WaterDay> pageResult = new PageResult<>();
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        PageHelper.startPage(pageNum,pageSize);
        List<WaterDay> listBetweenTimes = waterDayMapper.getListBetweenTimes(startTime, endTime, orgCode);
        pageResult.setData(listBetweenTimes);
        PageInfo<WaterDay> waterDayPageInfo = new PageInfo<>(listBetweenTimes);
        pageResult.setTotal(waterDayPageInfo.getTotal());
        return pageResult;
    }

    /**
     * 分页获取收支明细
     * @param orgCode 机构编码
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return PageResult<WaterDetailDto>
     */
    public PageResult<WaterDetailDto> waterDetailList(Integer orgCode,Integer pageNum,Integer pageSize,String startTime,String endTime){
        PageHelper.startPage(pageNum,pageSize);
        List<WaterDetailDto> waterDetailDtos = waterDayMapper.waterDetailList(orgCode,startTime,endTime);
        PageResult<WaterDetailDto> waterDetailList = new PageResult<>();
        PageInfo<WaterDetailDto> waterDetailDtoPageInfo = new PageInfo<>(waterDetailDtos);
        waterDetailList.setTotal(waterDetailDtoPageInfo.getTotal());
        waterDetailList.setPageNum(pageNum);
        waterDetailList.setPageSize(pageSize);
        waterDetailList.setData(waterDetailDtos);
        return waterDetailList;
    }

    /**
     * 获取报表
     * @param year year 2018
     * @param month month 2018-01
     * @param orgCode 机构编码1
     * @return List<WaterDay>
     */
    public List<WaterDay> getMonthYearBaobiao(String year,String month,Integer orgCode){
        if (StringUtils.isEmpty(year) && StringUtils.isNotEmpty(month)){
            return waterDayMapper.getListByYearMonth(orgCode,month+"-01");
        }else {
            return waterDayMapper.getListByYearGroupByMonth(orgCode,year+"-01-01");
        }
    }
    /**
     * 获取某天收益和那天所在月的收益
     * @param orgCode 机构
     * @param date 时间
     * @return WaterDayAndMonthCountDto
     */
    public WaterDayAndMonthCountDto getWaterDayAndMonthCount(String date,Integer orgCode){
        return waterDayMapper.getWaterDayAndMonthCount(orgCode,date);
    }

    public ShouzhiCountDto getShouzhiCountDto(String startTime, String endTime, Integer orgCode){
        return waterDayMapper.getShouzhiCountBetweenDateAndDate(orgCode,startTime,endTime);
    }

    public WaterDay firstWaterDay(Integer orgCode){
        Example example = new Example(WaterDay.class);
        example.createCriteria().andEqualTo("orgCode",orgCode);
        example.orderBy(" date").asc();
        List<WaterDay> waterDays = waterDayMapper.selectByExample(example);
        if (waterDays.size()!=0){
            return waterDays.get(0);
        }else {
            return null;
        }
    }
}
