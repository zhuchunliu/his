package com.acmed.his.service;

import com.acmed.his.UserApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * WxManagerTest
 *
 * @author jimson
 * @date 2018/3/7
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = UserApplication.class)
public class WxManagerTest {

    @Autowired
    private WxManager wxManager;
    @Test
    public void getOpenid() {
    }

    @Test
    public void getBaseAccessToken() {
        try {
            wxManager.wxUserInfo("ogmDPv0svLruqLy5d96QpihwVu-Y",null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //"7_3DbbsbCAZNzxFUcsjtj2kv-zehJpykrjnkDjWt68jbDCEAnekELHJmeRuL81SL8ODqNkE7nZC53uzzGdCm52HUfbI1JRDyUwyWYzQjmZ3PusZBDasygszlW0mXEFP1hNEydTNy5g4wZQiovoJRCgAFAVDO";
    }
}