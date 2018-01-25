package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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


    @Test
    public void getPreByApplyId(){
        System.err.println(JSONObject.toJSON(prescriptionManager.getPreByApplyId("6d00d31e2497473a9eeb3c2a0f04fd832uvz")));
    }

}
