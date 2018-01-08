package com.acmed.his.service;

import com.acmed.his.dao.DrugDayMapper;
import com.acmed.his.dao.InspectDayMapper;
import com.acmed.his.dao.OrgMapper;
import com.acmed.his.model.Org;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 报表
 * Created by Darren on 2018-01-05
 **/
@Service
public class DrugInspectReportManager {

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private DrugDayMapper drugDayMapper;

    @Autowired
    private InspectDayMapper inspectDayMapper;

    /**
     * 统计药品使用情况
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void statisDrug(){
        List<Org> orgList = orgMapper.selectAll();
        orgList.forEach(obj->{
            drugDayMapper.staticDrug(obj.getOrgCode());
        });

    }


    /**
     * 统计检查项目使用情况
     */
    @Scheduled(cron = "0 0 1 * * ?")
    private void statisInspect(){
        List<Org> orgList = orgMapper.selectAll();
        orgList.forEach(obj->{
            inspectDayMapper.staticInspect(obj.getOrgCode());
        });
    }
}
