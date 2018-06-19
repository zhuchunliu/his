package com.acmed.his.service;

import com.acmed.his.dao.MedicalRecordTplMapper;
import com.acmed.his.model.MedicalRecordTpl;
import com.acmed.his.model.dto.MedicalRecordTplDto;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MedicalRecordTplManager
 *
 * @author jimson
 * @date 2018/1/19
 */
@Service
public class MedicalRecordTplManager {
    @Autowired
    private MedicalRecordTplMapper medicalRecordTplMapper;

    /**
     * 添加病例模板 返回id
     * @param medicalRecordTpl
     * @return
     */
    public int add(MedicalRecordTpl medicalRecordTpl){
        String tplName = medicalRecordTpl.getTplName();
        if (StringUtils.isNotEmpty(tplName)){
            medicalRecordTpl.setTplNamePinYin(PinYinUtil.getPinYinHeadChar(tplName));
        }
        if (StringUtils.isEmpty(medicalRecordTpl.getIsPublic())){
            medicalRecordTpl.setIsPublic("0");
        }
        medicalRecordTpl.setRemoved("0");
        medicalRecordTpl.setIsValid("1");
        medicalRecordTpl.setCreateAt(LocalDateTime.now().toString());
        medicalRecordTpl.setModifyAt(LocalDateTime.now().toString());
        medicalRecordTplMapper.insert(medicalRecordTpl);
        return medicalRecordTpl.getId();
    }

    /**
     * 更新模板
     * @param medicalRecordTpl
     * @return
     */
    public int updateMedicalRecordTpl(MedicalRecordTpl medicalRecordTpl){
        String tplName = medicalRecordTpl.getTplName();
        if (StringUtils.isNotEmpty(tplName)){
            medicalRecordTpl.setTplNamePinYin(PinYinUtil.getPinYinHeadChar(tplName));
        }
        medicalRecordTpl.setModifyAt(LocalDateTime.now().toString());
        medicalRecordTpl.setCreateBy(null);
        medicalRecordTpl.setCreateAt(null);
        return medicalRecordTplMapper.updateByPrimaryKeySelective(medicalRecordTpl);
    }

    /**
     * 查询
     * @param medicalRecordTpl
     * @return
     */
    public List<MedicalRecordTplDto> getByParam(MedicalRecordTpl medicalRecordTpl){
        return medicalRecordTplMapper.selectByParam(medicalRecordTpl);
    }

    /**
     * 查询
     * @param medicalRecordTpl
     * @return
     */
    public PageResult<MedicalRecordTplDto> getByParamByPage(MedicalRecordTpl medicalRecordTpl, Integer pageNum, Integer pageSize){
        PageResult<MedicalRecordTplDto> result = new PageResult<>();
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        PageHelper.startPage(pageNum,pageSize);
        List<MedicalRecordTplDto> medicalRecordTplDtos = medicalRecordTplMapper.selectByParam(medicalRecordTpl);
        PageInfo<MedicalRecordTplDto> medicalRecordTplPageInfo = new PageInfo<>(medicalRecordTplDtos);
        result.setData(medicalRecordTplDtos);
        result.setTotal(medicalRecordTplPageInfo.getTotal());
        return result;
    }

    public MedicalRecordTpl medicalRecordTplDetail(Integer id){
        return medicalRecordTplMapper.selectByPrimaryKey(id);
    }

    public Integer getForbiddenMedicalRecordTotal(Integer userId) {
        return medicalRecordTplMapper.getForbiddenMedicalRecordTotal(userId);
    }
}
