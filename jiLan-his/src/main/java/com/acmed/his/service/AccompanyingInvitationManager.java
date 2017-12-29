package com.acmed.his.service;

import com.acmed.his.dao.AccompanyingInvitationMapper;
import com.acmed.his.model.AccompanyingInvitation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * AccompanyingInvitationManager
 *
 * @author jimson
 * @date 2017/12/27
 */
@Service
@Transactional
public class AccompanyingInvitationManager {
    @Autowired
    private AccompanyingInvitationMapper accompanyingInvitationMapper;

    /**
     * 根据患者id 查询邀请记录
     * @param patientId 患者id
     * @return AccompanyingInvitation
     */
    public AccompanyingInvitation getBypatientId(String patientId){
        AccompanyingInvitation accompanyingInvitation = new AccompanyingInvitation();
        accompanyingInvitation.setPatientId(patientId);
        return accompanyingInvitationMapper.selectOne(accompanyingInvitation);
    }

    /**
     * 天机邀请记录
     * @param accompanyingInvitation 参数
     * @return int
     */
    public int addAccompanyingInvitation(AccompanyingInvitation accompanyingInvitation){
        AccompanyingInvitation param = new AccompanyingInvitation();
        param.setPatientId(accompanyingInvitation.getPatientId());
        List<AccompanyingInvitation> select = accompanyingInvitationMapper.select(param);
        if (select.size()!=0){
            return 0;
        }else {
            accompanyingInvitation.setUserId(null);
            return accompanyingInvitationMapper.insert(accompanyingInvitation);
        }
    }
}
