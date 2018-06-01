package com.acmed.his.api;

import com.acmed.his.HisApplication;
import com.acmed.his.model.fzw.FZWServicePackage;
import com.acmed.his.pojo.mo.FZWOrderMo;
import com.acmed.his.service.FZWOrderManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.util.ResponseResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * FZWOrderApiTest
 *
 * @author jimson
 * @date 2018/5/22
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class FZWOrderApiTest {

    @Autowired
    private FZWOrderApi fZWOrderApi;

    @Test
    public void servicePackageList() {
        ResponseResult<List<FZWServicePackage>> listResponseResult = fZWOrderApi.servicePackageList();
        System.err.println(listResponseResult);
    }

    @Test
    public void save() {
        AccessInfo info = new AccessInfo();
        info.setPatientId("123456789test");
        FZWOrderMo fzwOrderMo = new FZWOrderMo();
        fzwOrderMo.setDoctor("王医生");
        fzwOrderMo.setId("20b81dfb6da644fa8a5a8a000499e8c0");
        fzwOrderMo.setEmail("123@qq.com");
        fzwOrderMo.setExamDate("2018-01-02");
        fzwOrderMo.setFZWServicePackageId(1);
        fzwOrderMo.setHospital("测试医院");
        fzwOrderMo.setMobile("12345678");
        fzwOrderMo.setName("王老六");
        //fzwOrderMo.setRemark("备注");
        fzwOrderMo.setSex("男");
        fZWOrderApi.save(info,fzwOrderMo);
    }
}