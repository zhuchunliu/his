package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.dao.DrugDayMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created by Darren on 2018-01-09
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class ReportManagerTest {

    @Autowired
    private ReportInspectManager inspectManager;

    @Autowired
    private ReportDrugManager drugManager;

    @Autowired
    private ReportWorkloadManager workloadManager;

    @Autowired
    private DrugDayMapper drugDayMapper;

    @Test
    public void statisInspect(){
        inspectManager.statisInspect();
    }

    @Test
    public void statisSale(){
        drugDayMapper.staticSale(1,
                LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 00:00:00")),
                LocalDateTime.now().minusDays(2).format(DateTimeFormatter.ofPattern("yyyy-MM-dd 23:59:59")));

        drugManager.statisSale();
    }

    @Test
    public void statisPurchase(){
        drugManager.statisPurchase();
    }

    @Test
    public void statisWorkload(){
        workloadManager.statisWorkload();
    }


}
