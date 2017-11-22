package com.acmed.his.service;

import com.acmed.his.dao.MedicalRecordMapper;
import com.acmed.his.model.MedicalRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * MedicalRecordManager
 *
 * @author jimson
 * @date 2017/11/21
 */
@Service
public class MedicalRecordManager {
    @Autowired
    private MedicalRecordMapper medicalRecordMapper;

    /**
     * 添加病例
     * @param medicalRecord 参数
     * @return 0 失败 1成功
     */
    public int addMedicalRecord(MedicalRecord medicalRecord){
        Date now = new Date();
        medicalRecord.setCreateAt(now);
        medicalRecord.setModifyAt(now);
        return medicalRecordMapper.insert(medicalRecord);
    }

    /**
     * 查询病例列表   两个参数不能全为空
     * @param patientId 病人id
     * @param doctorId 医生id
     * @return List<MedicalRecord>
     */
    public List<MedicalRecord> getMedicalRecordListByPatientId(Integer patientId,Integer doctorId){
        List<MedicalRecord> list = new ArrayList<>();
        if(patientId == null && doctorId == null){
            return list;
        }
        Example example = new Example(MedicalRecord.class);
        example.setOrderByClause("id desc");
        if (doctorId == null){
            // 查看用户的所有病例
            example.createCriteria().andEqualTo("patientId",patientId);
        }
        if (patientId == null){
            example.createCriteria().andEqualTo("createBy",doctorId);
        }
        if (patientId != null && doctorId != null){
            example.createCriteria().andEqualTo("createBy",doctorId).andEqualTo("patientId",patientId);
        }
        return medicalRecordMapper.selectByExample(example);
    }


}