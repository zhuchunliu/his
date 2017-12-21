package com.acmed.his.service;

import com.acmed.his.dao.ScheduleMapper;
import com.acmed.his.model.Schedule;
import com.acmed.his.model.dto.ScheduleDto;
import com.acmed.his.pojo.mo.ScheduleMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.DateTimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

/**
 * Created by Darren on 2017-12-20
 **/
@Service
public class ScheduleManager {

    @Autowired
    private ScheduleMapper scheduleMapper;

    /**
     * 设置用户的排班信息
     * @param mo
     * @param user
     */
    @Transactional
    public void update(ScheduleMo mo, UserInfo user) {
        Schedule schedule = scheduleMapper.getRecentSchedule(mo.getUserid());
        int week = LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).getDayOfWeek().getValue();

        if(null == schedule){ //第一次 直接添加
            schedule = new Schedule();
            schedule.setCreateAt(LocalDateTime.now().toString());
            schedule.setCreateBy(user.getId().toString());
            this.init(mo,schedule,week);
            scheduleMapper.insert(schedule);
            return;
        }

        if(DateTimeUtil.parsetLocalDate(schedule.getStartTime()).equals(//更新排班信息
                LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).minusDays(week-1))){
            schedule.setModifyAt(LocalDateTime.now().toString());
            schedule.setModifyBy(user.getId().toString());
            this.init(mo,schedule,week);
            scheduleMapper.updateByPrimaryKey(schedule);
            return;
        }

        if(schedule.getCircle().equals("1")){// 作废之前轮询的排班信息，重新排班

            //设置新的排班周期信息【除了当前天的排班，其他都参考之前周的排班】
            Schedule newSchedule = new Schedule();
            BeanUtils.copyProperties(schedule,newSchedule,"id");
            this.init(mo,newSchedule,week);
            scheduleMapper.insert(newSchedule);

            //关闭老的轮训信息表
            schedule.setEndTime(LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).minusDays(week).toString());
            schedule.setCircle("0");
            schedule.setModifyBy(user.getId().toString());
            schedule.setModifyAt(LocalDateTime.now().toString());
            scheduleMapper.updateByPrimaryKey(schedule);
        }else{
            //其余情况：默认新增新的排班信息
            schedule = new Schedule();
            schedule.setCreateAt(LocalDateTime.now().toString());
            schedule.setCreateBy(user.getId().toString());
            this.init(mo,schedule,week);
            scheduleMapper.insert(schedule);
        }

    }

    private void init(ScheduleMo mo,Schedule schedule,int week){
        schedule.setUserid(mo.getUserid());
        schedule.setCircle(mo.getCircle());
        schedule.setStartTime(LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).minusDays(week-1).toString());
        if(mo.getCircle().equals("0")){//没有开启循环，则设置截止时间
            schedule.setEndTime(LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).plusDays(7-week).toString());
        }else{
            schedule.setEndTime(null);
        }
        switch (week){
            case 1:
                schedule.setMonday(mo.getType());
                break;
            case 2:
                schedule.setTuesday(mo.getType());
                break;
            case 3:
                schedule.setWednesday(mo.getType());
                break;
            case 4:
                schedule.setThursday(mo.getType());
                break;
            case 5:
                schedule.setFriday(mo.getType());
                break;
            case 6:
                schedule.setSaturday(mo.getType());
                break;
            case 7:
                schedule.setSunday(mo.getType());
                break;
        }
    }


    /**
     * 获取排班列表
     *
     * @param deptId
     * @param userId
     * @param startTime
     * @param endTime
     */
    public List<ScheduleDto> getScheduleList(Integer orgCode ,Integer deptId, Integer userId, String startTime, String endTime) {

        if(StringUtils.isEmpty(startTime)){//不传入开始时间，则为当前这一周
            int week = LocalDateTime.now().getDayOfWeek().getValue();
            startTime = LocalDateTime.now().minusDays(week-1).toString();
            endTime = LocalDateTime.now().plusDays(7-week).toString();

        }
        return scheduleMapper.getScheduleList(orgCode , deptId, userId, startTime, endTime);

    }

    /**
     * 复制上周排班列表
     * @param userIds
     * @return
     */
    public List<ScheduleDto> getScheduleList(String userIds) {
        int week = LocalDateTime.now().getDayOfWeek().getValue();
        String startTime = LocalDateTime.now().minusDays(week-1).toString();
        String endTime =  LocalDateTime.now().plusDays(7-week).toString();
        return scheduleMapper.getPreScheduleList(startTime, endTime,userIds.split(","));
    }
}
