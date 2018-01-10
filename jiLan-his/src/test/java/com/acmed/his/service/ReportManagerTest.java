package com.acmed.his.service;

import com.acmed.his.HisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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

    @Test
    public void statisInspect(){
        inspectManager.statisInspect();
    }

    @Test
    public void statisSale(){
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
