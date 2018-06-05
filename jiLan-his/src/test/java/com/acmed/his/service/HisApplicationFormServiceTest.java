package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.HisApplicationForm;
import com.acmed.his.util.PageResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * HisApplicationFormServiceTest
 *
 * @author jimson
 * @date 2018/6/4
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class HisApplicationFormServiceTest {

    @Autowired
    private HisApplicationFormService hisApplicationFormService;
    @Test
    public void getByStatus() {
        PageResult<HisApplicationForm> byStatus = hisApplicationFormService.getByStatus(1,1,1);
        System.err.println(byStatus);
    }

    @Test
    public void createHisApplicationForm() {
        HisApplicationForm hisApplicationForm = new HisApplicationForm();
        hisApplicationForm.setHospital("测试医院1");
        hisApplicationForm.setMobile("手机号1");
        hisApplicationForm.setLinkman("联系人1");
        hisApplicationForm.setAddress("地址1");
        hisApplicationFormService.createHisApplicationForm(hisApplicationForm);
    }

    @Test
    public void updateStatus() {
        hisApplicationFormService.updateStatus("80669bb5c4304ac195855786bc752554",3,"123");
    }
}