package com.acmed.his.dao;

import com.acmed.his.model.MedicalRecord;
import com.acmed.his.model.dto.MedicalReDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface MedicalRecordMapper extends TkMapper<MedicalRecord>{
    /**
     * 获取患者就诊记录
     * @param patientId 患者id
     * @return 患者就诊记录
     */
    List<MedicalReDto> getMedicalReDto(@Param("patientId")Integer patientId);

    List<MedicalRecord> selectByMedicalRecord(MedicalRecord medicalRecord);
}
