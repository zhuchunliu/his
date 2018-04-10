package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.AccompanyingInvitationMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.AccompanyingInvitation;
import com.acmed.his.model.User;
import com.acmed.his.model.dto.InvitationDto;
import com.acmed.his.util.PageResult;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
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

    @Autowired
    private UserManager userManager;

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
        if ((StringUtils.isEmpty(accompanyingInvitation.getPatientId()) && accompanyingInvitation.getUserId() == null) || (StringUtils.isNotEmpty(accompanyingInvitation.getPatientId()) && accompanyingInvitation.getUserId() != null)){
            throw new BaseException(StatusCode.ERROR_PARAM);
        }

        Example example = new Example(AccompanyingInvitation.class);
        example.createCriteria().andEqualTo("patientId",accompanyingInvitation.getPatientId()).orEqualTo("userId",accompanyingInvitation.getUserId());
        List<AccompanyingInvitation> select = accompanyingInvitationMapper.selectByExample(example);
        if (select.size()!=0){
            return 0;
        }else {
            accompanyingInvitation.setUserId(null);
            return accompanyingInvitationMapper.insert(accompanyingInvitation);
        }
    }

    /**
     * 查看医生邀请的医生ids
     * @param userId
     * @return
     */
    public PageResult<InvitationDto> selectAccompanyingInvitationByUserId(Integer userId,Integer pageNum,Integer pageSize){
        PageResult<InvitationDto> result = new PageResult<>();
        PageHelper.startPage(pageNum,pageSize);
        List<InvitationDto> invitationDtoByUserId = accompanyingInvitationMapper.getInvitationDtoByUserId(userId);
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        result.setData(invitationDtoByUserId);
        PageInfo<InvitationDto> invitationDtoPageInfo = new PageInfo<>(invitationDtoByUserId);
        result.setTotal(invitationDtoPageInfo.getTotal());
        return result;
    }




}
