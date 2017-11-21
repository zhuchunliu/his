package com.acmed.his.service;

import com.acmed.his.dao.*;
import com.acmed.his.model.AdviceTpl;
import com.acmed.his.model.DiagnosisTpl;
import com.acmed.his.model.PrescriptionTpl;
import com.acmed.his.model.PrescriptionTplItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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



    /**
     * 获取诊断模板列表
     * @param orgCode 机构编码
     * @return
     */
    public List<DiagnosisTpl> getDiagnosisTplList(Integer orgCode){
        return diagnosisTplMapper.getDiagnosisTplList(orgCode);
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
    public void delDiagnosisTpl(Integer id){
        diagnosisTplMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增/编辑 诊断模板
     * @param diagnosisTpl 诊断模板
     * @return
     */
    public void saveDiagnosisTpl(DiagnosisTpl diagnosisTpl){
        if(null == diagnosisTpl.getId()){
            diagnosisTplMapper.insert(diagnosisTpl);
        }else{
            diagnosisTplMapper.updateByPrimaryKey(diagnosisTpl);
        }
    }

    /**
     * 获取医嘱模板列表
     * @param orgCode 机构编码
     * @return
     */
    public List<AdviceTpl> getAdviceTplList(Integer orgCode){
        return adviceTplMapper.getAdviceTplList(orgCode);
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
    public void delAdviceTpl(Integer id){
        adviceTplMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增/编辑 医嘱模板
     * @param adviceTpl 医嘱模板
     * @return
     */
    public void saveAdviceTpl(AdviceTpl adviceTpl){
        if(null == adviceTpl.getId()){
            adviceTplMapper.insert(adviceTpl);
        }else{
            adviceTplMapper.updateByPrimaryKey(adviceTpl);
        }
    }

    /**
     * 获取处方模板列表
     * @param orgCode 机构编码
     * @return
     */
    public List<PrescriptionTpl> getPrescripTplList(Integer orgCode){
        return prescriptionTplMapper.getPrescripTplList(orgCode);
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
    public void delPrescripTpl(Integer id){
        prescriptionTplItemMapper.deleteByTplId(id);
        prescriptionTplMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增/编辑 医嘱模板
     * @param prescriptionTpl 处方模板
     * @return
     */
    public void savePrescripTpl(PrescriptionTpl prescriptionTpl){
        if(null == prescriptionTpl.getId()){
            prescriptionTplMapper.insert(prescriptionTpl);
        }else{
            prescriptionTplMapper.updateByPrimaryKey(prescriptionTpl);
        }
    }


    /**
     * 获取处方模板详情列表
     * @param tplId 处方模板主键
     * @return
     */
    public List<PrescriptionTplItem> getPrescripTplItemList(Integer tplId){
        return prescriptionTplItemMapper.getPrescripTplItemList(tplId);
    }

    /**
     * 获取处方模板详情
     * @param id 处方模板详情主键
     * @return
     */
    public PrescriptionTplItem getPrescripTplItem(Integer id){
        return prescriptionTplItemMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除处方模板详情
     * @param id 处方模板详情主键
     * @return
     */
    public void delPrescripTplItem(Integer id){
        prescriptionTplItemMapper.deleteByPrimaryKey(id);
    }

    /**
     * 新增/编辑 医嘱模板详情
     * @param prescriptionTplItem 处方模板详情
     * @return
     */
    public void savePrescriptionTplItem(PrescriptionTplItem prescriptionTplItem){
        if(null == prescriptionTplItem.getId()){
            prescriptionTplItemMapper.insert(prescriptionTplItem);
        }else{
            prescriptionTplItemMapper.updateByPrimaryKey(prescriptionTplItem);
        }
    }

}
