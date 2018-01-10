package com.acmed.his.api;

import com.acmed.his.service.ReportWorkloadManager;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Darren on 2018-01-09
 **/
@Api(tags = "报表-工作量统计")
@RequestMapping("/report")
@RestController
public class ReportWorkloadApi {

    @Autowired
    private ReportWorkloadManager workloadManager;
}
