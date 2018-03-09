package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.ApplyMapper;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.User;
import com.acmed.his.model.dto.ScheduleApplyDto;
import com.acmed.his.model.dto.ScheduleDto;
import com.acmed.his.pojo.mo.ScheduleMo;
import com.acmed.his.pojo.vo.ScheduleApplyVo;
import com.acmed.his.pojo.vo.ScheduleVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.ScheduleManager;
import com.acmed.his.service.UserManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.DateTimeUtil;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2017-12-20
 **/
@RestController
@Api(tags = "排班管理",description = "排班管理")
@RequestMapping("/schedule")
public class ScheduleApi {

    @Autowired
    private ScheduleManager scheduleManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private UserManager userManager;

    @Autowired
    private ApplyMapper applyMapper;

    @ApiOperation(value = "设置排班信息")
    @PostMapping("/save")
    public ResponseResult update(@RequestBody ScheduleMo mo,
                                   @AccessToken AccessInfo info){
        scheduleManager.update(mo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "获取排班列表")
    @GetMapping("/list")
    public ResponseResult<ScheduleVo> list(@ApiParam("科室主键") @RequestParam(value = "deptId",required = false) Integer deptId,
                                           @ApiParam("人员主键") @RequestParam(value = "userId",required = false) Integer userId,
                                           @ApiParam("日期") @RequestParam(value = "time",required = false) String time,
                                           @AccessToken AccessInfo info){
        List<ScheduleDto> sourceList = scheduleManager.getScheduleList(info.getUser().getOrgCode(),deptId,userId,time);
        List<ScheduleVo> list = new ArrayList<>();
        sourceList.forEach(obj->{
            ScheduleVo vo = new ScheduleVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取上周排班信息[复制上周用]")
    @GetMapping("/copy")
    public ResponseResult<ScheduleVo> getPreviousList(@ApiParam("科室主键") @RequestParam(value = "deptId",required = false) Integer deptId,
                                                      @ApiParam("挂号日期 默认当前周") @RequestParam(value = "date",required = false) String date,
                                                      @AccessToken AccessInfo info){
        List<ScheduleDto> sourceList = scheduleManager.getPreviousList(info.getUser(), date,deptId);
        List<ScheduleVo> list = new ArrayList<>();
        sourceList.forEach(obj->{
            ScheduleVo vo = new ScheduleVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "挂号医生列表")
    @GetMapping("/apply")
    public ResponseResult<ScheduleApplyVo> list(@ApiParam("医院code 不传默认账号所在机构") @RequestParam(value = "orgCode",required = false) Integer orgCode,
                                                @ApiParam("挂号日期 默认当前周") @RequestParam(value = "date",required = false) String date,
                                                @ApiParam("科室主键") @RequestParam(value = "deptId",required = false) Integer deptId,
                                                @AccessToken AccessInfo info){

        if(orgCode == null){
            orgCode = info.getUser().getOrgCode();
        }

        LocalDate startDate = Optional.ofNullable(date).map(DateTimeUtil::parsetLocalDate).orElse(LocalDate.now());
        LocalDate endDate = Optional.ofNullable(date).map(DateTimeUtil::parsetLocalDate).orElse(LocalDate.now().plusDays(6));

        List<ScheduleApplyDto> sourceList = scheduleManager.getScheduleApplyList(orgCode,deptId,date);

        Map<String,DicItem> scheduleMap = Maps.newHashMap();
        baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.SCHEDULE.getCode()).forEach(obj->
            scheduleMap.put(obj.getDicItemCode(),obj));

        Map<String,String> dutyMap = Maps.newHashMap();
        baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DUTY.getCode()).forEach(obj->
                dutyMap.put(obj.getDicItemCode(),obj.getDicItemName()));

        Map<String,String> diagnosisMap = Maps.newHashMap();
        baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DIAGNOSIS_LEVEL.getCode()).forEach(obj->
                diagnosisMap.put(obj.getDicItemCode(),obj.getDicItemName()));


        List<ScheduleApplyVo> list = Lists.newArrayList();
        String[] weekarr = new String[]{"一","二","三","四","五","六","日"};
        for(LocalDate child = startDate;!child.isAfter(endDate);child=child.plusDays(1)){



            ScheduleApplyVo vo = new ScheduleApplyVo();
            vo.setWeek("周"+weekarr[child.getDayOfWeek().getValue()-1]);
            vo.setDate(child.format(DateTimeFormatter.ofPattern("MM/dd")));
            List<ScheduleApplyVo.ScheduleApplyDetail> detailList = Lists.newArrayList();
            for(ScheduleApplyDto dto : sourceList){
                if(DateTimeUtil.parsetLocalDate(dto.getStartTime()).isAfter(child)
                        || DateTimeUtil.parsetLocalDate(dto.getEndTime()).isBefore(child)){
                    continue;
                }

                User user = userManager.getUserDetail(dto.getUserid());//过滤禁用医生
                if(!StringUtils.isNotEmpty(user.getStatus()) && "0".equals(user.getStatus())){
                    continue;
                }

                String schedule = null;
                switch (child.getDayOfWeek().getValue()){
                    case 1: schedule = dto.getMonday();break;
                    case 2: schedule = dto.getTuesday();break;
                    case 3: schedule = dto.getWednesday();break;
                    case 4: schedule = dto.getThursday();break;
                    case 5: schedule = dto.getFriday();break;
                    case 6: schedule = dto.getSaturday();break;
                    case 7: schedule = dto.getSunday();break;
                }
                if(StringUtils.isEmpty(schedule)){
                    continue;
                }
                if(child.isEqual(LocalDate.now())  //同一天，按照时间过滤
                        && StringUtils.isNotEmpty(scheduleMap.get(schedule).getEndTime())
                        && !scheduleMap.get(schedule).getEndTime().equals("00:00:00")
                        && !LocalTime.parse(scheduleMap.get(schedule).getEndTime()).isAfter(LocalTime.now())){
                    continue;
                }
                ScheduleApplyVo.ScheduleApplyDetail detail = new ScheduleApplyVo().new ScheduleApplyDetail();
                BeanUtils.copyProperties(dto,detail);
                detail.setDiagnosLevelName(Optional.ofNullable(dto.getDiagnosLevel()).map(val->diagnosisMap.get(val)).orElse(null));
                detail.setDutyName(Optional.ofNullable(dto.getDuty()).map(val->dutyMap.get(val)).orElse(null));
                detail.setSchedule(scheduleMap.get(schedule).getDicItemName());
                detail.setApplyNum(user.getApplyNum());
                detail.setOccupyNum(applyMapper.getDoctorApplyNum(dto.getUserid(),DateTimeUtil.getBeginDate(child.toString()),DateTimeUtil.getEndDate(child.toString())));
                detail.setAppointmentTime(child.toString());
                detailList.add(detail);
            }

            vo.setDetailList(detailList);
            list.add(vo);
        }
        return ResponseUtil.setSuccessResult(list);
    }

    public static void main(String[] args) {
        System.err.println(LocalDateTime.now().plusDays(6).getDayOfWeek().getValue());
    }


}
