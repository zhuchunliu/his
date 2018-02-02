package com.acmed.his.service;

import com.acmed.his.dao.PatientItemMapper;
import com.acmed.his.model.Patient;
import com.acmed.his.model.PatientItem;
import com.acmed.his.model.dto.PatientItemDto;
import com.acmed.his.pojo.mo.PatientItemMo;
import com.acmed.his.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * PatientItemManager
 *
 * @author jimson
 * @date 2018/1/23
 */
@Service
public class PatientItemManager {

    @Autowired
    private PatientItemMapper patientItemMapper;

    @Autowired
    private PatientManager patientManager;
    /**
     * 添加子表
     * @param patientItem
     * @return
     */
    public int addPatinetItem(PatientItem patientItem){
        patientItem.setId(Optional.ofNullable(patientItem.getId()).orElse(UUIDUtil.generate()));
        patientItem.setBlackFlag(0);
        if (StringUtils.isNotEmpty(patientItem.getPatientName())){
            patientItem.setInputCode(PinYinUtil.getPinYinHeadChar(patientItem.getPatientName()));
        }
        if (StringUtils.isNotEmpty(patientItem.getIdCard())){
            int i = IdCardUtil.idCardToAge(patientItem.getIdCard());
            patientItem.setAge(i);
            LocalDate localDate = IdCardUtil.idCardToDate(patientItem.getIdCard());
            if(localDate!=null){
                patientItem.setDateOfBirth(localDate.toString());
            }
        }
        patientItem.setModifyBy(null);
        patientItem.setModifyAt(null);
        patientItem.setCreateAt(LocalDateTime.now().toString());
        return patientItemMapper.insert(patientItem);
    }


    /**
     * 查询患者库
     * @param pageBase 条件
     * @return PageResult<PatientItem>
     */
    public PageResult<PatientItemDto> getPatientBlacklistByPage(PageBase<PatientItemMo> pageBase){
        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<PatientItemDto> select = patientItemMapper.getByMohu(pageBase.getParam());
        PageInfo<PatientItemDto> patientPageInfo = new PageInfo<>(select);
        PageResult<PatientItemDto> pageResult = new PageResult<>();
        pageResult.setData(select);
        pageResult.setTotal(patientPageInfo.getTotal());
        pageResult.setPageNum(pageNum);
        pageResult.setPageSize(pageSize);
        return pageResult;
    }

    /**
     * 患者库黑名单操作
     * @param orgCode 机构code
     * @param userId 用户id
     * @param id 患者库id
     * @param blackFlag 删除标记
     * @return 0失败 1成功
     */
    public int updatePatientItemBlackFlag(Integer orgCode,Integer userId,String id,Integer blackFlag){
        PatientItem patientItem = new PatientItem();
        patientItem.setId(id);
        patientItem.setOrgCode(orgCode);
        PatientItem patientItem1 = patientItemMapper.selectOne(patientItem);
        if (patientItem1!=null){
            patientItem.setBlackFlag(blackFlag);
            patientItem.setModifyAt(LocalDateTime.now().toString());
            patientItem.setModifyBy(userId.toString());
            return patientItemMapper.updateByPrimaryKeySelective(patientItem);
        }else {
            return 0;
        }
    }

    public List<PatientItem> patientItems(PatientItem patientItem){
        return patientItemMapper.select(patientItem);
    }

    /**
     * 更新
     * @param patientItem
     * @return
     */
    public int updatePatientItem(PatientItem patientItem){
        if (StringUtils.isNotEmpty(patientItem.getPatientName())){
            patientItem.setInputCode(PinYinUtil.getPinYinHeadChar(patientItem.getPatientName()));
        }
        if (StringUtils.isNotEmpty(patientItem.getIdCard())){
            int i = IdCardUtil.idCardToAge(patientItem.getIdCard());
            patientItem.setAge(i);
            LocalDate localDate = IdCardUtil.idCardToDate(patientItem.getIdCard());
            if(localDate!=null){
                patientItem.setDateOfBirth(localDate.toString());
            }
        }
        patientItem.setModifyAt(LocalDateTime.now().toString());
        return patientItemMapper.updateByPrimaryKeySelective(patientItem);
    }

    public PatientItem getById(String id){
        return patientItemMapper.selectByPrimaryKey(id);
    }

    public PatientItemDto getPatientItemDtoById(String id){
        PatientItemDto patientItemDto = new PatientItemDto();
        PatientItem patientItem = patientItemMapper.selectByPrimaryKey(id);
        if (patientItem == null){
            return null;
        }
        BeanUtils.copyProperties(patientItem,patientItemDto);
        Patient patientById = patientManager.getPatientById(patientItem.getPatientId());
        patientItemDto.setAvatar(patientById.getAvatar());
        return patientItemDto;
    }

    public PatientItem getPatientByIdCard(String idCard,Integer orgCode) {
        PatientItem param = new PatientItem();
        param.setIdCard(idCard);
        param.setOrgCode(orgCode);
        return patientItemMapper.selectOne(param);
    }
}
