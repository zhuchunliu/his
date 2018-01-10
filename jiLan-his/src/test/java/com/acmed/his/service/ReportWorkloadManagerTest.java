package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.WorkloadDay;
import com.acmed.his.model.dto.WorkloadDayAndTotalDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * ReportWorkloadManagerTest
 *
 * @author jimson
 * @date 2018/1/10
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class ReportWorkloadManagerTest {
    @Autowired
    private ReportWorkloadManager reportWorkloadManager;

    @Test
    public void getWorkloadList() {
    }

    @Test
    public void getWorkloadListGroupByDate() {
        List<WorkloadDay> workloadListGroupByDate = reportWorkloadManager.getWorkloadListGroupByDate(1, "2017-01-01", "2018-10-10");
        System.err.println(workloadListGroupByDate.toString());
    }

    @Test
    public void getWorkloadDayAndTotal() {
        WorkloadDayAndTotalDto workloadDayAndTotal = reportWorkloadManager.getWorkloadDayAndTotal(1, "2018-01-08");
        System.err.println(workloadDayAndTotal.toString());
    }
}