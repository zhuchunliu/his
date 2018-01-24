package com.acmed.his.service;

import com.acmed.his.dao.PatientItemMapper;
import com.acmed.his.model.PatientItem;
import com.acmed.his.pojo.mo.PatientItemMo;
import com.acmed.his.util.*;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        }
        patientItem.setCreateAt(LocalDateTime.now().toString());
        return patientItemMapper.insert(patientItem);
    }


    /**
     * 查询患者库
     * @param pageBase 条件
     * @param blackFlag 拉黑标记  0 未拉黑 1 已拉黑
     * @return PageResult<PatientItem>
     */
    public PageResult<PatientItem> getPatientBlacklistByPage(PageBase<PatientItemMo> pageBase, Integer blackFlag){

        Integer pageNum = pageBase.getPageNum();
        Integer pageSize = pageBase.getPageSize();
        PageHelper.startPage(pageNum,pageSize);
        List<PatientItem> select = patientItemMapper.getByMohu(pageBase.getParam());
        PageInfo<PatientItem> patientPageInfo = new PageInfo<>(select);
        PageResult<PatientItem> pageResult = new PageResult<>();
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
        }
        patientItem.setModifyAt(LocalDateTime.now().toString());
        return patientItemMapper.updateByPrimaryKeySelective(patientItem);
    }

    public PatientItem getById(String id){
        return patientItemMapper.selectByPrimaryKey(id);
    }

}
