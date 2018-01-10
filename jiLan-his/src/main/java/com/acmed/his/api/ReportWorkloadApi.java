package com.acmed.his.api;

import com.acmed.his.model.WorkloadDay;
import com.acmed.his.service.ReportWorkloadManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Darren on 2018-01-09
 **/
@Api(tags = "报表-工作量统计")
@RequestMapping("/report")
@RestController
public class ReportWorkloadApi {

    @Autowired
    private ReportWorkloadManager workloadManager;


    @ApiOperation(value = "工作量统计,预约明细统计")
    @GetMapping("/list")
    public ResponseResult<WorkloadDay> getWorkloadList(@ApiParam("开始时间") @RequestParam(value = "startTime",required = false) String startTime,
                                                        @ApiParam("结束时间") @RequestParam(value = "endTime",required = false) String endTime,
                                                       @ApiParam("医生姓名") @RequestParam(value = "name",required = false) String userName,
                                                        @ApiParam("排序方式 1:接诊人数(默认排序); 2:门诊收入") @RequestParam(value = "type",defaultValue = "1" ,required = false) Integer type,
                                                        @AccessToken AccessInfo info){

        List<WorkloadDay> list =  workloadManager.getWorkloadList(info.getUser().getOrgCode(),userName,startTime,endTime,type);
        return ResponseUtil.setSuccessResult(list);
    }


}
