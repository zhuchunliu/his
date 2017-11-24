package com.acmed.his.service;

import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.pojo.mo.AdviceTplMo;
import com.acmed.his.pojo.mo.DiagnosisTplMo;
import com.acmed.his.pojo.mo.PrescriptionTplMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 模板
 * Created by Darren on 2017-11-20
 **/
@Service
public class TemplateManager {

    @Autowired
    private DiagnosisTplMapper diagnosisTplMapper;

    @Autowired
    private AdviceTplMapper adviceTplMapper;

    @Autowired
    private PrescriptionTplMapper prescriptionTplMapper;

    @Autowired
    private PrescriptionTplItemMapper prescriptionTplItemMapper;

    @Autowired
    private DrugMapper drugMapper;



    /**
     * 获取诊断模板列表
     * @param orgCode 机构编码
     * @return
     */
    public List<DiagnosisTpl> getDiagnosisTplList(Integer orgCode){
        return diagnosisTplMapper.getDiagnosisTplList(orgCode,"1");
    }

    /**
     * 获取诊断模板详情
     * @param id 诊断模板主键
     * @return
     */
    public DiagnosisTpl getDiagnosisTpl(Integer id){
        return diagnosisTplMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除诊断模板
     * @param id 诊断模板主键
     * @return
     */
    public void delDiagnosisTpl(Integer id, UserInfo userInfo){
        DiagnosisTpl diagnosisTpl = diagnosisTplMapper.selectByPrimaryKey(id);
        diagnosisTpl.setIsValid("0");
        diagnosisTpl.setModifyAt(LocalDateTime.now().toString());
        diagnosisTpl.setModifyBy(userInfo.getId().toString());
        diagnosisTplMapper.updateByPrimaryKey(diagnosisTpl);
    }

    /**
     * 新增/编辑 诊断模板
     * @param mo 诊断模板
     * @return
     */
    public void saveDiagnosisTpl(DiagnosisTplMo mo, UserInfo userInfo){
        if(null == mo.getId()){
            DiagnosisTpl diagnosisTpl = new DiagnosisTpl();
            BeanUtils.copyProperties(mo,diagnosisTpl);
            diagnosisTpl.setCreateAt(LocalDateTime.now().toString());
            diagnosisTpl.setCreateBy(userInfo.getId().toString());
            diagnosisTpl.setOrgCode(userInfo.getOrgCode());
            diagnosisTpl.setIsValid("1");
            diagnosisTplMapper.insert(diagnosisTpl);
        }else{
            DiagnosisTpl diagnosisTpl = diagnosisTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,diagnosisTpl);
            diagnosisTpl.setModifyAt(LocalDateTime.now().toString());
            diagnosisTpl.setModifyBy(userInfo.getId().toString());
            diagnosisTpl.setIsValid("1");
            diagnosisTplMapper.updateByPrimaryKey(diagnosisTpl);
        }
    }

    /**
     * 获取医嘱模板列表
     * @param orgCode 机构编码
     * @return
     */
    public List<AdviceTpl> getAdviceTplList(Integer orgCode){
        return adviceTplMapper.getAdviceTplList(orgCode,"1");
    }

    /**
     * 获取医嘱模板详情
     * @param id 诊断模板主键
     * @return
     */
    public AdviceTpl getAdviceTpl(Integer id){
        return adviceTplMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除医嘱模板
     * @param id 诊断模板主键
     * @return
     */
    public void delAdviceTpl(Integer id,UserInfo userInfo){
        AdviceTpl adviceTpl = adviceTplMapper.selectByPrimaryKey(id);
        adviceTpl.setModifyAt(LocalDateTime.now().toString());
        adviceTpl.setModifyBy(userInfo.getId().toString());
        adviceTpl.setIsValid("0");
        adviceTplMapper.updateByPrimaryKey(adviceTpl);
    }

    /**
     * 新增/编辑 医嘱模板
     * @param mo 医嘱模板
     * @return
     */
    public void saveAdviceTpl(AdviceTplMo mo, UserInfo userInfo){
        if(null == mo.getId()){
            AdviceTpl adviceTpl = new AdviceTpl();
            BeanUtils.copyProperties(mo,adviceTpl);
            adviceTpl.setCreateAt(LocalDateTime.now().toString());
            adviceTpl.setCreateBy(userInfo.getId().toString());
            adviceTpl.setOrgCode(userInfo.getOrgCode());
            adviceTpl.setIsValid("1");
            adviceTplMapper.insert(adviceTpl);
        }else{
            AdviceTpl adviceTpl = adviceTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,adviceTpl);
            adviceTpl.setIsValid("1");
            adviceTpl.setModifyAt(LocalDateTime.now().toString());
            adviceTpl.setModifyBy(userInfo.getId().toString());
            adviceTplMapper.updateByPrimaryKey(adviceTpl);
        }
    }

    /**
     * 获取处方模板列表
     * @param orgCode 机构编码
     * @return
     */
    public List<PrescriptionTpl> getPrescripTplList(Integer orgCode){
        return prescriptionTplMapper.getPrescripTplList(orgCode,"1");
    }

    /**
     * 获取处方模板详情
     * @param id 处方模板主键
     * @return
     */
    public PrescriptionTpl getPrescripTpl(Integer id){
        return prescriptionTplMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除处方模板
     * @param id 处方模板主键
     * @return
     */
    @Transactional
    public void delPrescripTpl(Integer id,UserInfo userInfo){
        PrescriptionTpl prescriptionTpl = prescriptionTplMapper.selectByPrimaryKey(id);
        prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
        prescriptionTpl.setModifyBy(userInfo.getId().toString());
        prescriptionTpl.setIsValid("0");
        prescriptionTplMapper.updateByPrimaryKey(prescriptionTpl);
    }

    /**
     * 新增/编辑 医嘱模板
     * @param mo 处方模板
     * @return
     */
    public boolean savePrescripTpl(PrescriptionTplMo mo, UserInfo userInfo){
        PrescriptionTpl prescriptionTpl = null;
        List<PrescriptionTplItem> preItemList = new ArrayList<>();
        if(null == mo.getId()){
            prescriptionTpl = new PrescriptionTpl();
            BeanUtils.copyProperties(mo,prescriptionTpl);
            prescriptionTpl.setOrgCode(userInfo.getOrgCode());
            prescriptionTpl.setCreateAt(LocalDateTime.now().toString());
            prescriptionTpl.setCreateBy(userInfo.getId().toString());
            prescriptionTpl.setIsValid("1");
            prescriptionTplMapper.insert(prescriptionTpl);

            prescriptionTpl = prescriptionTplMapper.selectRecentTpl(prescriptionTpl);

        }else{
            prescriptionTpl = prescriptionTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,prescriptionTpl);
            prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
            prescriptionTpl.setModifyBy(userInfo.getId().toString());
            prescriptionTplMapper.updateByPrimaryKey(prescriptionTpl);

            Example example = new Example(PrescriptionTplItem.class);
            example.createCriteria().andEqualTo("tplId",prescriptionTpl.getId());
            preItemList = prescriptionTplItemMapper.selectByExample(example);
        }
        if( null == prescriptionTpl){
            return false;
        }
        int tplId = prescriptionTpl.getId();
        mo.getItemList().forEach((obj)->{
            PrescriptionTplItem item = new PrescriptionTplItem();
            BeanUtils.copyProperties(obj,item);
            item.setTplId(tplId);
            item.setDrugName(Optional.ofNullable(obj.getDrugId()).
                    map(id->drugMapper.selectByPrimaryKey(id)).map((drug -> drug.getName())).orElse(null));
            prescriptionTplItemMapper.insert(item);
        });

        preItemList.forEach(obj->prescriptionTplItemMapper.deleteByPrimaryKey(obj));
        return true;
    }


    /**
     * 获取处方模板详情列表
     * @param tplId 处方模板主键
     * @return
     */
    public List<PrescriptionTplItem> getPrescripTplItemList(Integer tplId){
        return prescriptionTplItemMapper.getPrescripTplItemList(tplId);
    }


}
