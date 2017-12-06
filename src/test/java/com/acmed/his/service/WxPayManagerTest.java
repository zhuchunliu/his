package com.acmed.his.service;

import com.acmed.his.HisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * WxPayManagerTest
 *
 * @author jimson
 * @date 2017/12/6
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class WxPayManagerTest {
    @Autowired
    private WxPayManager wxPayManager;
    @Test
    public void unifiedorder() throws Exception {
        wxPayManager.unifiedorder("測試","oTAaixD73vuSyUaLNKc8cDFS5ncw","123123123123",null,1,"8.8.8.8","JSAPI",null);
    }

}