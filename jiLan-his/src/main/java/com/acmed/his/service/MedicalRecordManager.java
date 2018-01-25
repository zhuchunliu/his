package com.acmed.his.service;

import com.acmed.his.dao.MedicalRecordMapper;
import com.acmed.his.model.Apply;
import com.acmed.his.model.MedicalRecord;
import com.acmed.his.model.dto.MedicalReDto;
import com.acmed.his.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private ApplyManager applyManager;

    /**
     * 添加病例
     * @param medicalRecord 参数
     * @return 0 失败 1成功
     */
    public int saveMedicalRecord(MedicalRecord medicalRecord){
        String id = medicalRecord.getId();
        if (StringUtils.isEmpty(id)){
            medicalRecord.setId(UUIDUtil.generate());
            String applyId = medicalRecord.getApplyId();
            Apply apply = applyManager.getApplyById(applyId);
            if (apply == null){
                return 0;
            }
            medicalRecord.setPatientId(apply.getPatientId());
            addMedicalRecord(medicalRecord);
            apply = new Apply();
            apply.setId(applyId);
            // 挂号单修改成已就诊
            apply.setStatus("1");
            applyManager.updateApply(apply);
            return 1;
        }else {
            return updateMedicalRecord(medicalRecord);
        }
    }

    public int updateMedicalRecord(MedicalRecord medicalRecord){
        String id = medicalRecord.getId();
        if(StringUtils.isEmpty(id)){
            return 0;
        }
        medicalRecord.setCreateBy(null);
        medicalRecord.setCreateAt(null);
        medicalRecord.setModifyBy(LocalDateTime.now().toString());
        return medicalRecordMapper.insert(medicalRecord);
    }

    public int addMedicalRecord(MedicalRecord medicalRecord){
        String id = medicalRecord.getId();
        if(StringUtils.isEmpty(id)){
            medicalRecord.setId(UUIDUtil.generate());
        }
        medicalRecord.setModifyBy(null);
        medicalRecord.setModifyAt(null);
        medicalRecord.setCreateAt(LocalDateTime.now().toString());
        return medicalRecordMapper.insert(medicalRecord);
    }

    /**
     * 查询病例列表   两个参数不能全为空
     * @param patientId 病人id
     * @param doctorId 医生id
     * @return List<MedicalRecord>
     */
    public List<MedicalRecord> getMedicalRecordListByPatientItemId(Integer patientId,Integer doctorId){
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

    public List<MedicalRecord> getMedicalRecordListByMedicalRecord(MedicalRecord medicalRecord){
        return medicalRecordMapper.selectByMedicalRecord(medicalRecord);
    }

    /**
     * 获取患者就诊记录
     * @param patientId 患者id
     * @return 患者就诊记录
     */
    public List<MedicalReDto> getMedicalReDtoList(Integer patientId){
        return medicalRecordMapper.getMedicalReDto(patientId);
    }

    /**
     * 根据的查询病例详情
     * @param id id
     * @return 病例详情
     */
    public MedicalRecord getMedicalRecordById(String id){
        return medicalRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据applyId查询病例详情
     * @param applyId id
     * @return 病例详情
     */
    public MedicalRecord getMedicalRecordByApplyId(String applyId){
        Example example = new Example(MedicalRecord.class);
        example.createCriteria().andEqualTo("applyId",applyId);

        return Optional.ofNullable(medicalRecordMapper.selectByExample(example)).filter(obj->0!=obj.size()).map(obj->obj.get(0)).orElse(null);
    }

}
