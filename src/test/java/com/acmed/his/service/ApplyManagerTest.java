package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.Apply;
import com.acmed.his.pojo.vo.ApplyDoctorVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * ApplyManagerTest
 *
 * @author jimson
 * @date 2017/11/23
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class ApplyManagerTest {

    @Autowired
    private ApplyManager applyManager;
    @Test
    public void addApply() throws Exception {
        Apply apply = new Apply();
        apply.setOrgCode(1);
        apply.setOrgName("和谐医院");
        apply.setPatientId(1);
        apply.setPatientName("王二");
        apply.setGender("1");
        apply.setPinYin("WE");
        apply.setDept(1);
        apply.setStatus("0");
        apply.setIsPaid("1");
        apply.setDeptName("内科");
        applyManager.addApply(apply);
    }

    @Test
    public void getApplyByPatientId() throws Exception {
        applyManager.getApplyByPatientId(1);
    }

    @Test
    public void getApplyById() throws Exception {
        applyManager.getApplyById(2);
    }

    @Test
    public void getApplyByOrgCode() throws Exception {
        applyManager.getApplyByOrgCode(1);
    }

    @Test
    public void getApplyByDeptIdAndStatus() throws Exception {
        applyManager.getApplyByDeptIdAndStatus(1,null);
    }

    @Test
    public void updateApply() throws Exception {
        Apply apply = new Apply();
        apply.setId(3);
        apply.setOrgName("和谐医院232332");
        applyManager.updateApply(apply);
    }

    @Test
    public void getApplyDoctorVoList() throws Exception {
        applyManager.getApplyDoctorVoList(1,"2017-12-05",null);
    }

    @Test
    public void getByPinyinOrName() throws Exception {
        List<ApplyDoctorVo> w = applyManager.getByPinyinOrName("七", null,1,new Date());
        System.err.println(w.size());
    }
}