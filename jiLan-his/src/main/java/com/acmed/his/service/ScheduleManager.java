package com.acmed.his.service;

import com.acmed.his.dao.ScheduleMapper;
import com.acmed.his.dao.UserMapper;
import com.acmed.his.model.Prescription;
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
import java.time.LocalDate;
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
     * @param deptId
     * @return
     */
    public List<ScheduleDto> getPreviousList(UserInfo userInfo,String date,Integer deptId) {
        LocalDateTime dateTime = Optional.ofNullable(date).map(DateTimeUtil::parsetLocalDate).orElse(LocalDateTime.now());
        int week = dateTime.getDayOfWeek().getValue();
        String preStartTime = dateTime.minusDays(week+7-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String preEndTime =  dateTime.minusDays(week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));

        String startTime = dateTime.minusDays(week-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime =  dateTime.plusDays(7-week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));
        if(dateTime.plusDays(7 - week).isBefore(LocalDateTime.now())) return Lists.newArrayList();//已过期的，不让复制
        List<ScheduleDto> preList =  scheduleMapper.getScheduleList(userInfo.getOrgCode(),deptId, null,preStartTime, preEndTime);
        for(ScheduleDto dto:preList){
            Schedule schedule = scheduleMapper.getRecentSchedule(dto.getUserid(),startTime,endTime);
            if(null == schedule){
                schedule = new Schedule();
                schedule.setUserid(dto.getUserid());
                if(!dateTime.minusDays(week-1).toLocalDate().isBefore(LocalDate.now())) schedule.setMonday(dto.getMonday());
                if(!dateTime.minusDays(week-2).toLocalDate().isBefore(LocalDate.now())) schedule.setTuesday(dto.getTuesday());
                if(!dateTime.minusDays(week-3).toLocalDate().isBefore(LocalDate.now())) schedule.setWednesday(dto.getWednesday());
                if(!dateTime.minusDays(week-4).toLocalDate().isBefore(LocalDate.now())) schedule.setThursday(dto.getThursday());
                if(!dateTime.minusDays(week-5).toLocalDate().isBefore(LocalDate.now())) schedule.setFriday(dto.getFriday());
                if(!dateTime.minusDays(week-6).toLocalDate().isBefore(LocalDate.now())) schedule.setSaturday(dto.getSaturday());
                if(!dateTime.minusDays(week-7).toLocalDate().isBefore(LocalDate.now())) schedule.setSunday(dto.getSunday());

                schedule.setStartTime(startTime);
                schedule.setEndTime(endTime);
                schedule.setCreateAt(LocalDateTime.now().toString());
                schedule.setCreateBy(userInfo.getId().toString());
                scheduleMapper.insert(schedule);
            }else {
                if(StringUtils.isEmpty(schedule.getMonday()) && !dateTime.minusDays(week-1).toLocalDate().isBefore(LocalDate.now())) schedule.setMonday(dto.getMonday());
                if(StringUtils.isEmpty(schedule.getTuesday()) && !dateTime.minusDays(week-2).toLocalDate().isBefore(LocalDate.now())) schedule.setTuesday(dto.getTuesday());
                if(StringUtils.isEmpty(schedule.getWednesday()) && !dateTime.minusDays(week-3).toLocalDate().isBefore(LocalDate.now())) schedule.setWednesday(dto.getWednesday());
                if(StringUtils.isEmpty(schedule.getThursday()) && !dateTime.minusDays(week-4).toLocalDate().isBefore(LocalDate.now())) schedule.setThursday(dto.getThursday());
                if(StringUtils.isEmpty(schedule.getFriday()) && !dateTime.minusDays(week-5).toLocalDate().isBefore(LocalDate.now())) schedule.setFriday(dto.getFriday());
                if(StringUtils.isEmpty(schedule.getSaturday()) && !dateTime.minusDays(week-6).toLocalDate().isBefore(LocalDate.now())) schedule.setSaturday(dto.getSaturday());
                if(StringUtils.isEmpty(schedule.getSunday()) && !dateTime.minusDays(week-7).toLocalDate().isBefore(LocalDate.now())) schedule.setSunday(dto.getSunday());
                schedule.setModifyAt(LocalDateTime.now().toString());
                schedule.setModifyBy(userInfo.getId().toString());
                scheduleMapper.updateByPrimaryKey(schedule);
            }
        }
        return scheduleMapper.getScheduleList(userInfo.getOrgCode(),deptId, null,startTime, endTime);
    }



    /**
     * 挂号，医生列表
     * @param orgCode
     * @param deptId
     * @return
     */
    public List<ScheduleApplyDto> getScheduleApplyList(Integer orgCode, Integer deptId,String date) {
        LocalDateTime dateTime = Optional.ofNullable(date).map(DateTimeUtil::parsetLocalDate).orElse(LocalDateTime.now());
        int week = dateTime.getDayOfWeek().getValue();
        String startTime = dateTime.minusDays(week-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime =  dateTime.plusDays(7-week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));

        return scheduleMapper.getScheduleApplyList(orgCode,deptId,startTime, endTime);
    }



    public static void main(String[] args) {
        int week = LocalDateTime.now().getDayOfWeek().getValue();
        String startTime = LocalDateTime.now().minusDays(week-1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        String endTime =  LocalDateTime.now().plusDays(7-week).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59"));

        String dd = LocalDateTime.now().minusDays(week-7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00"));
        System.err.println(week+" "+startTime+" "+endTime+"  "+dd);
        System.err.println(LocalDate.now().minusDays(week-6).isEqual(LocalDate.now()));
    }


}
