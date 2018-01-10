package com.acmed.his.service;

import com.acmed.his.dao.OrgMapper;
import com.acmed.his.dao.WorkloadDayMapper;
import com.acmed.his.model.Org;
import com.acmed.his.model.WorkloadDay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Darren on 2018-01-09
 **/
@Service
public class ReportWorkloadManager {

    private Logger logger = LoggerFactory.getLogger(ReportWorkloadManager.class);

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
            try {
                workloadDayMapper.statisWorkload(obj.getOrgCode(),
                        LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")),
                        LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
            }catch (Exception ex){
                logger.error("统计工作量出错，出错机构： "+obj.getOrgCode());
                logger.error(ex.getMessage(),ex);
            }
        });
    }


    /**
     * 工作量列表
     *
     * @param orgCode
     * @param startTime
     * @param endTime
     * @param type
     * @return
     */
    public List<WorkloadDay> getWorkloadList(Integer orgCode, String userName,String startTime, String endTime,Integer type) {
        return workloadDayMapper.getWorkloadList(orgCode,userName,startTime,endTime,type);
    }
}
