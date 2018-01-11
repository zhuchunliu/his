package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.InspectDayMapper;
import com.acmed.his.dao.InspectMapper;
import com.acmed.his.dao.OrgMapper;
import com.acmed.his.model.Org;
import com.acmed.his.model.dto.InspectDayDto;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Darren on 2018-01-08
 **/
@Service
public class ReportInspectManager {

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private InspectDayMapper inspectDayMapper;

    @Autowired
    private InspectMapper inspectMapper;

    /**
     * 统计检查项目使用情况
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void statisInspect(){
        List<Org> orgList = orgMapper.selectAll();
        orgList.forEach(obj->{
            inspectDayMapper.staticInspect(obj.getOrgCode(),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        });
    }

    /**
     * 获取检查统计列表
     *
     * @param orgCode
     * @param startTime
     * @param endTime
     * @return
     */
    public List<InspectDayDto> getInspectList(Integer orgCode, Integer num ,String startTime, String endTime) {

        return inspectDayMapper.getInspectList(orgCode,DicTypeEnum.INSPECT_CATEGORY.getCode(),num,startTime,endTime);
    }

    public Double getInspectFee(Integer orgCode, String startTime, String endTime) {
        return inspectDayMapper.getInspectFee(orgCode,startTime,endTime);
    }

    /**
     * 获取检查统计列表
     *
     * @param orgCode
     * @param startTime
     * @param endTime
     * @return
     */
    public List<InspectDayDto> getInspectDetailList(Integer orgCode, String startTime, String endTime,Integer pageNum, Integer pageSize) {
        PageHelper.offsetPage(pageNum,pageSize);
        return inspectMapper.getItemList(orgCode,DicTypeEnum.INSPECT_CATEGORY.getCode(),startTime,endTime);
    }

    public Integer getInspectDetailTotal(Integer orgCode, String startTime, String endTime) {
        return inspectMapper.getItemTotal(orgCode,startTime,endTime);
    }


}
