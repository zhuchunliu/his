package com.acmed.his.service;

import com.acmed.his.HisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Darren on 2017-11-20
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class AdviceTplManagerTest {

    @Autowired
    private AdviceTplManager adviceTplManager;


    @Test
    public void add(){
        adviceTplManager.add();
    }

    @Test
    public void info(){
        adviceTplManager.info().forEach((obj)->System.err.println(obj));
    }
}
