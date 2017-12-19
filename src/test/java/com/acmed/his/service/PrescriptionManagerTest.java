package com.acmed.his.service;

import com.acmed.his.HisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Darren on 2017-11-22
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class PrescriptionManagerTest {

    @Autowired
    private PrescriptionManager prescriptionManager;


    @Test
    public void getPreByApply() {
        prescriptionManager.getPreByApply("1").forEach((obj)->System.err.println(obj));
    }


}
