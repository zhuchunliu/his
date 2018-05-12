package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.WaterDay;
import com.acmed.his.model.dto.WaterDetailDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * WaterDayManagerTest
 *
 * @author jimson
 * @date 2018/1/5
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class WaterDayManagerTest {
    @Autowired
    private WaterDayManager waterDayManager;

    @Test
    public void paopi() throws ParseException {
        waterDayManager.paopi("20180511");
    }
    @Test
    public void task() throws ParseException {
        waterDayManager.task();
    }

    @Test
    public void waterDetailList() throws ParseException {
        List<WaterDetailDto> data = waterDayManager.waterDetailList(1, 1, 5, "20180104", "20180104").getData();
        System.err.println(data);
    }



    @Test
    public void getListBetweenTimes() {
        List<WaterDay> listBetweenTimes = waterDayManager.getListBetweenTimes("2017-10-01", "2018-01-06", 1);
        System.err.println(listBetweenTimes);
    }


    @Test
    public void getMonthYearBaobiao() {
        List<WaterDay> monthYearBaobiao = waterDayManager.getMonthYearBaobiao( null, "2018-01",1);
        System.err.println(monthYearBaobiao);
    }

    @Test
    public void getWaterDayAndMonthCount(){
        LocalDate today = LocalDate.now();
        LocalDate localDate = today.plusDays(1);
        String s = localDate.plusDays(1).toString();
        waterDayManager.getWaterDayAndMonthCount(s,1);
    }
}