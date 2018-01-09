package com.acmed.his.api;

import com.acmed.his.model.dto.InspectDayDto;
import com.acmed.his.pojo.mo.DrugMo;
import com.acmed.his.service.ReportInspectManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Darren on 2018-01-08
 **/
@Api(tags = "报表-检查项目统计")
@RequestMapping("/report")
@RestController
public class ReportInspectApi {

    @Autowired
    private ReportInspectManager inspectManager;

    @ApiOperation(value = "检查统计")
    @GetMapping("/inspect/list")
    public ResponseResult<InspectDayDto> getInspectList(@ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                     @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                     @AccessToken AccessInfo info){

        List<InspectDayDto> list =  inspectManager.getInspectList(info.getUser().getOrgCode(),startTime,endTime);
        return ResponseUtil.setSuccessResult(list);
    }


    @ApiOperation(value = "检查统计")
    @PostMapping("/inspect/detail")
    public ResponseResult<InspectDayDto> getInspectList(@RequestBody(required = false) PageBase pageBase,
                                                        @ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                        @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                        @AccessToken AccessInfo info){

        List<InspectDayDto> list =  inspectManager.getInspectDetailList(info.getUser().getOrgCode(),startTime,endTime,pageBase.getPageNum(), pageBase.getPageSize());
        int total = inspectManager.getInspectDetailTotal(info.getUser().getOrgCode(),startTime,endTime);
        PageResult pageResult = new PageResult();
        BeanUtils.copyProperties(pageBase,pageResult);
        pageResult.setTotal((long)total);
        pageResult.setData(list);
        return ResponseUtil.setSuccessResult(pageResult);
    }

}
