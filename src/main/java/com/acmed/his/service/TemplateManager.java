package com.acmed.his.service;

import com.acmed.his.dao.AdviceTplMapper;
import com.acmed.his.dao.DiagnosisTplMapper;
import com.acmed.his.dao.PrescriptionTplMapper;
import com.acmed.his.model.AdviceTpl;
import com.acmed.his.model.DiagnosisTpl;
import com.acmed.his.model.PrescriptionTpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void delPrescripTpl(Integer id){
        prescriptionTplMapper.deleteByPrimaryKey(id);
    }
}
