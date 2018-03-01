package com.acmed.his.service;

import com.acmed.his.dao.PatientCardMapper;
import com.acmed.his.model.PatientCard;
import com.acmed.his.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * PatientCardManager
 *
 * @author jimson
 * @date 2018/1/24
 */
@Service
public class PatientCardManager {
    @Autowired
    private PatientCardMapper patientCardMapper;

    public int add(PatientCard patientCard){
        patientCard.setModifyAt(null);
        patientCard.setModifyBy(null);
        patientCard.setCreateAt(LocalDateTime.now().toString());
        patientCard.setRemoved("0");
        String id = patientCard.getId();
        if (StringUtils.isEmpty(id)){
            patientCard.setId(UUIDUtil.generate());
        }
        return patientCardMapper.insert(patientCard);
    }

    public int update(PatientCard patientCard){
        String id = patientCard.getId();
        if (StringUtils.isEmpty(id)){
            return 0;
        }
        patientCard.setCreateAt(null);
        patientCard.setCreateBy(null);
        patientCard.setModifyAt(LocalDateTime.now().toString());
        return patientCardMapper.updateByPrimaryKeySelective(patientCard);
    }

    public List<PatientCard> getPatientCardList(PatientCard patientCard){
        return patientCardMapper.select(patientCard);
    }

    public PatientCard patientCardDetail(String id){
        return patientCardMapper.selectByPrimaryKey(id);
    }
}
