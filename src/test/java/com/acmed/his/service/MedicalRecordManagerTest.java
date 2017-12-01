package com.acmed.his.service;

import com.acmed.his.HisApplication;
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
}