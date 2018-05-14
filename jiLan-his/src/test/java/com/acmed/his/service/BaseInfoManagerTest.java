package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.Area;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * BaseInfoManagerTest
 *
 * @author jimson
 * @date 2018/5/14
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class BaseInfoManagerTest {

    @Autowired
    private BaseInfoManager baseInfoManager;
    @Test
    public void bsgcitys() {
        List<Area> bsgcitys = baseInfoManager.bsgcitys(820);
        System.err.println(bsgcitys);
    }
}