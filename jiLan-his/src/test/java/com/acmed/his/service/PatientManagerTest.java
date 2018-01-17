package com.acmed.his.service;

import com.acmed.his.HisApplication;
import com.acmed.his.model.Patient;
import com.acmed.his.model.PatientItem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * PatientManagerTest
 *
 * @author jimson
 * @date 2017/12/1
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HisApplication.class)
public class PatientManagerTest {
    @Autowired
    private PatientManager patientManager;
    @Test
    public void add() throws Exception {
        Patient patient = new Patient();
        patient.setUserName("王五");
        patient.setGender("0");
        patient.setAddress("江苏苏州");
        patient.setMobile("13288888888");
        patient.setWeight(new BigDecimal(66.66));
        patient.setProf("律师");
        patient.setIdCard("320588199010106666");
        patient.setSocialCard("123456789");
        patientManager.add(patient);
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void getPatientById() throws Exception {
    }

    @Test
    public void getPatientByOpenid() throws Exception {
    }

    @Test
    public void getPatientByIdCard() throws Exception {
    }

    @Test
    public void getPatientByUserName() throws Exception {
    }

    @Test
    public void wxRegistPatient() throws Exception {
    }

    @Test
    public void addPatinetItem(){
        PatientItem patientItem = new PatientItem();
        patientItem.setOrgCode(3);
        patientItem.setIdCard("335252525");
        patientItem.setAvatar("dfdsfdsfdsfsdfsd");
        patientItem.setMobile("1234");
        patientManager.addPatinetItem(patientItem);
    }
}