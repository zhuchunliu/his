package com.acmed.his.service;

import com.acmed.his.HisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * InsuranceOrderManagerTest
 *
 * @author jimson
 * @date 2018/4/19
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class InsuranceOrderManagerTest {
    @Autowired
    private InsuranceOrderManager insuranceOrderManager;
    @Test
    public void task(){
        insuranceOrderManager.task();
    }

}