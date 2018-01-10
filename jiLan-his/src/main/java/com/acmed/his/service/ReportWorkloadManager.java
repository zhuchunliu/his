package com.acmed.his.service;

import com.acmed.his.dao.OrgMapper;
import com.acmed.his.dao.WorkloadDayMapper;
import com.acmed.his.model.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Darren on 2018-01-09
 **/
@Service
public class ReportWorkloadManager {

    @Autowired
    private WorkloadDayMapper workloadDayMapper;

    @Autowired
    private OrgMapper orgMapper;

    /**
     * 统计工作量
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void statisWorkload(){
        List<Org> orgList = orgMapper.selectAll();
        orgList.forEach(obj->{
            workloadDayMapper.statisWorkload(obj.getOrgCode(),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        });
    }


}
