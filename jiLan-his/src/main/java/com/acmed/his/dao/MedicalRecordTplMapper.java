package com.acmed.his.dao;

import com.acmed.his.model.MedicalRecordTpl;
import com.acmed.his.util.TkMapper;

import java.util.List;

/**
 * MedicalRecordTplMapper
 *
 * @author jimson
 * @date 2018/1/19
 */
public interface MedicalRecordTplMapper extends TkMapper<MedicalRecordTpl>{
    /**
     * 条件查询
     * @param medicalRecordTpl
     * @return
     */
    List<MedicalRecordTpl> selectByParam(MedicalRecordTpl medicalRecordTpl);
}
