package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.MedicalRecord;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

/**
 * MedicalRecordManagerTest
 *
 * @author jimson
 * @date 2017/12/1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class MedicalRecordManagerTest {
    @Autowired
    private MedicalRecordManager medicalRecordManager;
    @Test
    public void addMedicalRecord() throws Exception {
    }

    @Test
    public void getMedicalRecordListByPatientId() throws Exception {
    }

    @Test
    public void getMedicalReDtoList() throws Exception {
        System.out.println(medicalRecordManager.getMedicalReDtoList(1));
    }

    @Test
    public void getMedicalRecordById() throws Exception {
        medicalRecordManager.getMedicalRecordById(1);
    }
    @Test
    public void getMedicalRecordListByMedicalRecord(){
        MedicalRecord medicalRecord = new MedicalRecord();
        //medicalRecord.setCreateBy("2");
        medicalRecord.setOrgCode(1);
        medicalRecord.setPatientItemId("54398d7568a94e50ba6716250a10e52aX5DZ");
        System.err.println(medicalRecordManager.getMedicalRecordListByMedicalRecord(medicalRecord));
    }
}