package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.WaterDay;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        waterDayManager.paopi("20180104");
    }
}