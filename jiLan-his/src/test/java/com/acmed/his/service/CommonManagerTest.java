package com.acmed.his.service;

import com.acmed.his.HisApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;

/**
 * Created by Darren on 2017-11-20
 **/
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class CommonManagerTest {

    @Autowired
    private CommonManager commonManager;

    @Test
    public void getFormatVal() {
        System.err.print(commonManager.getFormatVal("doctor","D000000"));
    }

    public static void main(String[] args) {
        System.err.println(LocalDate.now().toString());
    }
}
