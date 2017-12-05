package com.acmed.his.service;

import com.acmed.his.dao.MedicalRecordMapper;
import com.acmed.his.model.Apply;
import com.acmed.his.model.MedicalRecord;
import com.acmed.his.model.dto.MedicalReDto;
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
    public int addMedicalRecord(MedicalRecord medicalRecord){
        Integer id = medicalRecord.getId();
        if (id==null){
            Integer applyId = medicalRecord.getApplyId();
            Apply apply = applyManager.getApplyById(applyId);
            if (apply == null){
                return 0;
            }
            medicalRecord.setPatientId(apply.getPatientId());
            medicalRecord.setOrgCode(apply.getOrgCode());
            medicalRecord.setDept(apply.getDept());
            medicalRecord.setDeptName(apply.getDeptName());
            String now = LocalDateTime.now().toString();
            medicalRecord.setCreateAt(now);
            medicalRecord.setModifyAt(now);
            medicalRecordMapper.insert(medicalRecord);
            apply = new Apply();
            apply.setId(applyId);
            // 挂号单修改成已就诊
            apply.setStatus("1");
            applyManager.updateApply(apply);
            return 1;
        }else {
            return medicalRecordMapper.updateByPrimaryKeySelective(medicalRecord);
        }
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
    public MedicalRecord getMedicalRecordById(Integer id){
        return medicalRecordMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据applyId查询病例详情
     * @param applyId id
     * @return 病例详情
     */
    public MedicalRecord getMedicalRecordByApplyId(Integer applyId){
        Example example = new Example(MedicalRecord.class);
        example.createCriteria().andEqualTo("applyId",applyId);

        return Optional.ofNullable(medicalRecordMapper.selectByExample(example)).filter(obj->0!=obj.size()).map(obj->obj.get(0)).orElse(null);
    }
}
