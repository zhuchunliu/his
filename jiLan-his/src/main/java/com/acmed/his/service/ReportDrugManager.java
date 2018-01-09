package com.acmed.his.service;

import com.acmed.his.dao.*;
import com.acmed.his.model.Org;
import com.acmed.his.model.dto.DrugDayDetailDto;
import com.acmed.his.model.dto.DrugDayDto;
import com.acmed.his.model.dto.PurchaseDayDetailDto;
import com.acmed.his.model.dto.PurchaseDayDto;
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
public class ReportDrugManager {

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private DrugDayMapper drugDayMapper;

    @Autowired
    private PurchaseDayMapper purchaseDayMapper;

    @Autowired
    private PrescriptionItemMapper itemMapper;

    @Autowired
    private PurchaseItemMapper purchaseItemMapper;

    /**
     * 统计药品使用情况
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void statisSale(){
        List<Org> orgList = orgMapper.selectAll();
        orgList.forEach(obj->{
            drugDayMapper.staticSale(obj.getOrgCode(),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        });

    }

    /**
     * 统计药品采购入库情况
     */
    @Scheduled(cron = "0 1 0 * * ?")
    public void statisPurchase(){
        List<Org> orgList = orgMapper.selectAll();
        orgList.forEach(obj->{
            purchaseDayMapper.statisPurchase(obj.getOrgCode(),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")),
                    LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));
        });

    }



    /**
     * 查询药品出库统计报表
     *
     * @param orgCode
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     */
    public List<DrugDayDto> getDrugDayList(Integer orgCode, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return drugDayMapper.getDrugDayList(orgCode,startTime,endTime);

    }

    /**
     *
     * @param orgCode
     * @param startTime
     * @param endTime
     * @return
     */
    public int getDrugDayTotal(Integer orgCode, String startTime, String endTime) {
        return drugDayMapper.getDrugDayTotal(orgCode,startTime,endTime);
    }

    /**
     * 出库明细
     * @param orgCode
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<DrugDayDetailDto> getDrugDayDetailList(Integer orgCode, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return itemMapper.getItemList(orgCode,startTime,endTime);
    }

    public Integer getDrugDayDetailTotal(Integer orgCode, String startTime, String endTime) {
        return itemMapper.getItemTotal(orgCode,startTime,endTime);
    }

    /**
     * 统计入库列表
     * @param orgCode
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<PurchaseDayDto> getPurchaseDayList(Integer orgCode, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return purchaseDayMapper.getPurchaseDayList(orgCode,startTime,endTime);
    }

    public Integer getPurchaseDayTotal(Integer orgCode, String startTime, String endTime) {
        return purchaseDayMapper.getPurchaseDayTotal(orgCode,startTime,endTime);
    }

    /**
     * 入库明细
     * @param orgCode
     * @param startTime
     * @param endTime
     * @param pageNum
     * @param pageSize
     * @return
     */
    public List<PurchaseDayDetailDto> getPurchaseDayDetailList(Integer orgCode, String startTime, String endTime, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        return purchaseItemMapper.getItemList(orgCode,startTime,endTime);
    }

    public Integer getPurchaseDayDetailTotal(Integer orgCode, String startTime, String endTime) {
        return purchaseItemMapper.getItemTotal(orgCode,startTime,endTime);
    }
}
