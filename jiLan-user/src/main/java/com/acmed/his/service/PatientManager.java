package com.acmed.his.service;


import com.acmed.his.dao.PatientMapper;
import com.acmed.his.model.Patient;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * PatientManager
 *
 * @author jimson
 * @date 2017/11/20
 */
@Service
@Transactional
public class PatientManager {

    @Autowired
    private PatientMapper patientMapper;

    /**
     * 根据openid 查询患者详情
     * @param openid 微信openid
     * @return Patient
     */
    public Patient getPatientByOpenid(String openid){
        Patient param = new Patient();
        param.setOpenid(openid);
        return patientMapper.selectOne(param);
    }


}
