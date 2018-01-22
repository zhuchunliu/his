package com.acmed.his.api;

import com.acmed.his.dao.InspectDayMapper;
import com.acmed.his.dao.InspectMapper;
import com.acmed.his.model.dto.InspectDayDto;
import com.acmed.his.pojo.mo.ReportQueryMo;
import com.acmed.his.pojo.vo.InspectDayVo;
import com.acmed.his.service.ReportInspectManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.acmed.his.util.DateTimeUtil.parsetLocalDate;

/**
 * Created by Darren on 2018-01-08
 **/
@Api(tags = "报表-检查项目统计")
@RequestMapping("/report")
@RestController
public class ReportInspectApi {

    @Autowired
    private ReportInspectManager inspectManager;

    @Autowired
    private InspectMapper inspectMapper;

    @Autowired
    private InspectDayMapper inspectDayMapper;


    @ApiOperation("当天检查项实时统计")
    @GetMapping("/inspect/recent")
    @ApiResponse(code = 100,message = "num:检查项目,fee:检查收入")
    public ResponseResult getRecentStatis(@AccessToken AccessInfo info){
        Integer num = inspectMapper.getCurrentDayNum(info.getUser().getOrgCode());
        Double fee =inspectMapper.getCurrentDayFee(info.getUser().getOrgCode());
        Map<String,Object> map = Maps.newHashMap();
        map.put("num",num);
        map.put("fee",fee);
        return ResponseUtil.setSuccessResult(map);

    }

    @ApiOperation("收支概况")
    @GetMapping("/inspect/survey")
    @ApiResponse(code = 100,message = "num:检查项目,fee:检查收入")
    public ResponseResult getSurveyStatis(@ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                          @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                          @AccessToken AccessInfo info){

        startTime = Optional.ofNullable(startTime).map(DateTimeUtil::getBeginDate).orElse(null);
        endTime = Optional.ofNullable(endTime).map(DateTimeUtil::getEndDate).orElse(null);

        Integer num = inspectMapper.getSurveyNum(info.getUser().getOrgCode(),startTime,endTime);
        Double fee =inspectMapper.getSurveyFee(info.getUser().getOrgCode(),startTime,endTime);
        Map<String,Object> map = Maps.newHashMap();
        map.put("num",num);
        map.put("fee",fee);
        return ResponseUtil.setSuccessResult(map);

    }

    @ApiOperation(value = "检查统计")
    @GetMapping("/inspect/list")
    public ResponseResult<InspectDayVo> getInspectList(@ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                       @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                       @ApiParam("显示数量,默认：6个") @RequestParam(value = "num",defaultValue = "6",required = false) Integer num,
                                                       @AccessToken AccessInfo info){

        startTime = Optional.ofNullable(startTime).map(DateTimeUtil::getBeginDate).orElse(null);
        endTime = Optional.ofNullable(endTime).map(DateTimeUtil::getEndDate).orElse(null);

        List<InspectDayDto> list =  inspectManager.getInspectList(info.getUser().getOrgCode(),num,startTime,endTime);

        Double fee = inspectManager.getInspectFee(info.getUser().getOrgCode(),startTime,endTime);

        InspectDayVo vo = new InspectDayVo();
        vo.setTotalFee(fee);
        vo.setInspectList(list);

        if(!StringUtils.isEmpty(startTime) && !StringUtils.isEmpty(endTime) && !startTime.equals(endTime)) {
            List<Map<String, Object>> dayList = inspectDayMapper.getInspectDayList(info.getUser().getOrgCode(), startTime, endTime);
            Map<String, Object> dayMap = Maps.newHashMap();
            dayList.forEach(obj -> {
                dayMap.put(obj.get("date").toString(),obj.get("fee"));
            });

            LocalDateTime startDate = parsetLocalDate(startTime);
            LocalDateTime endDate = parsetLocalDate(endTime);
            List<InspectDayVo.DayFee> dayFeeList = Lists.newArrayList();

            while (true) {
                InspectDayVo.DayFee dayFee = new InspectDayVo().new DayFee();
                if (startDate.isAfter(endDate)) {
                    break;
                }
                if (dayMap.containsKey(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))) {
                    dayFee.setDate(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    dayFee.setFee(dayMap.get(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))));
                } else {
                    dayFee.setDate(startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    dayFee.setFee("0.00");
                }
                dayFeeList.add(dayFee);
                startDate = startDate.plusDays(1);
            }
            vo.setDayFeeList(dayFeeList);
        }
        return ResponseUtil.setSuccessResult(vo);
    }


    @ApiOperation(value = "检查统计")
    @PostMapping("/inspect/detail")
    public ResponseResult<InspectDayDto> getInspectList(@RequestBody(required = false) PageBase<ReportQueryMo> pageBase,
                                                        @AccessToken AccessInfo info){

        List<InspectDayDto> list =  inspectManager.getInspectDetailList(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null),pageBase.getPageNum(), pageBase.getPageSize());
        int total = inspectManager.getInspectDetailTotal(info.getUser().getOrgCode(),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getStartTime()).orElse(null),
                Optional.ofNullable(pageBase.getParam()).map(obj->obj.getEndTime()).orElse(null));
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);

    }




}
