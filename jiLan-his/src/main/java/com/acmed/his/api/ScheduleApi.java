package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.model.dto.ScheduleDto;
import com.acmed.his.pojo.mo.ScheduleMo;
import com.acmed.his.pojo.vo.ScheduleVo;
import com.acmed.his.service.ScheduleManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2017-12-20
 **/
@RestController
@Api(tags = "排班管理",description = "排班管理")
@RequestMapping("/schedule")
public class ScheduleApi {

    @Autowired
    private ScheduleManager scheduleManager;

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
                                           @ApiParam("周开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                           @ApiParam("周结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                           @AccessToken AccessInfo info){
        List<ScheduleDto> sourceList = scheduleManager.getScheduleList(info.getUser().getOrgCode(),deptId,userId,startTime,endTime);
        List<ScheduleVo> list = new ArrayList<>();
        sourceList.forEach(obj->{
            ScheduleVo vo = new ScheduleVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "复制上周")
    @GetMapping("/previous")
    public ResponseResult<ScheduleVo> list(@ApiParam("人员主键集合，逗号间隔") @RequestParam(value = "userIds",required = false) String userIds){
        List<ScheduleDto> sourceList = scheduleManager.getScheduleList(userIds);
        List<ScheduleVo> list = new ArrayList<>();
        sourceList.forEach(obj->{
            ScheduleVo vo = new ScheduleVo();
            BeanUtils.copyProperties(obj,vo);
            list.add(vo);
        });
        return ResponseUtil.setSuccessResult(list);
    }



}
