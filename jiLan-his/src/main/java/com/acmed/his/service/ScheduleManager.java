package com.acmed.his.service;

import com.acmed.his.dao.ScheduleMapper;
import com.acmed.his.dao.UserMapper;
import com.acmed.his.model.Schedule;
import com.acmed.his.model.User;
import com.acmed.his.model.dto.ScheduleApplyDto;
import com.acmed.his.model.dto.ScheduleDto;
import com.acmed.his.pojo.mo.ScheduleMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.DateTimeUtil;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        int week = LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).getDayOfWeek().getValue();
        String startTime = LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).minusDays(week-1).
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime = LocalDateTime.ofInstant(mo.getDate().toInstant(),ZoneId.systemDefault()).plusDays(7-week).
                format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        Schedule schedule = scheduleMapper.getRecentSchedule(mo.getUserid(),startTime,endTime);

        if(null == schedule){ //第一次 直接添加
            schedule = new Schedule();
            schedule.setUserid(mo.getUserid());
            schedule.setStartTime(startTime);
            schedule.setEndTime(endTime);
            schedule.setCreateAt(LocalDateTime.now().toString());
            schedule.setCreateBy(user.getId().toString());
            this.init(mo,schedule,week);
            scheduleMapper.insert(schedule);
        }else{//直接修改
            schedule.setModifyAt(LocalDateTime.now().toString());
            schedule.setModifyBy(user.getId().toString());
            this.init(mo,schedule,week);
            scheduleMapper.updateByPrimaryKey(schedule);
        }
    }

    private void init(ScheduleMo mo,Schedule schedule,int week){

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
     * @param time
     */
    public List<ScheduleDto> getScheduleList(Integer orgCode ,Integer deptId, Integer userId, String time) {

        LocalDateTime date = Optional.ofNullable(time).map(obj->DateTimeUtil.parsetLocalDate(obj)).orElse(LocalDateTime.now());
        int week = date.getDayOfWeek().getValue();
        String  startTime= date.minusDays(week-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime = date.plusDays(7-week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        return scheduleMapper.getScheduleList(orgCode , deptId,
                Optional.ofNullable(userId).map(obj->Lists.newArrayList(userId.toString())).orElse(null), startTime, endTime);

    }

    /**
     * 复制上周排班列表
     * @param userIds
     * @return
     */
    public List<ScheduleDto> getPreviousList(String userIds,String time) {
        LocalDateTime date = Optional.ofNullable(time).map(obj->DateTimeUtil.parsetLocalDate(obj)).orElse(LocalDateTime.now());
        int week = date.getDayOfWeek().getValue();
        String startTime = date.minusDays(week+7-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime =  date.minusDays(week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        return scheduleMapper.getScheduleList(null,null, StringUtils.isEmpty(userIds)?null:new ArrayList<String>(Arrays.asList(userIds.split(","))),startTime, endTime);
    }

    /**
     * 挂号，医生列表
     * @param orgCode
     * @param deptId
     * @return
     */
    public List<ScheduleApplyDto> getScheduleApplyList(Integer orgCode, Integer deptId) {
        int week = LocalDateTime.now().getDayOfWeek().getValue();
        String startTime = LocalDateTime.now().minusDays(week-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime =  LocalDateTime.now().plusDays(7-week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        return scheduleMapper.getScheduleApplyList(orgCode,deptId,startTime, endTime);
    }



    public static void main(String[] args) {
        int week = LocalDateTime.now().getDayOfWeek().getValue();
        String startTime = LocalDateTime.now().minusDays(week-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime =  LocalDateTime.now().plusDays(7-week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        System.err.println(week+" "+startTime+" "+endTime);
    }


}
