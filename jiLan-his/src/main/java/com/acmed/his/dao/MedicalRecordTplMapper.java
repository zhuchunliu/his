package com.acmed.his.dao;

import com.acmed.his.model.MedicalRecordTpl;
import com.acmed.his.model.dto.MedicalRecordTplDto;
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
    List<MedicalRecordTplDto> selectByParam(MedicalRecordTpl medicalRecordTpl);
}
