package com.acmed.his.service;

import com.acmed.his.HisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * QNManagerTest
 *
 * @author jimson
 * @date 2018/4/26
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class QNManagerTest {

    @Autowired
    private QNManager qnManager;

    @Test
    public void getUpToken() {
    }

    @Test
    public void uploadByUrl() {
    }

    @Test
    public void delete() {
        qnManager.delete("124/124.jpgA8060999-C9F7-4F48-B8CA-B210B2DF5EE1.png");
    }
}