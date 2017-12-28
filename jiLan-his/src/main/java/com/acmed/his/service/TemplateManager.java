package com.acmed.his.service;

import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.pojo.mo.AdviceTplMo;
import com.acmed.his.pojo.mo.DiagnosisTplMo;
import com.acmed.his.pojo.mo.PrescriptionTplMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PinYinUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
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

    @Autowired
    private InspectTplMapper inspectTplMapper;



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
    public List<PrescriptionTpl> getPrescripTplList(Integer orgCode,String category){
        return prescriptionTplMapper.getPrescripTplList(orgCode,"1",category);
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
    @Transactional
    public boolean savePrescripTpl(PrescriptionTplMo mo, UserInfo userInfo){
        PrescriptionTpl prescriptionTpl = null;
        if(null == mo.getId()){
            prescriptionTpl = new PrescriptionTpl();
            BeanUtils.copyProperties(mo,prescriptionTpl);
            prescriptionTpl.setCategory((null != mo.getItemList() && mo.getItemList().size()!=0)?"1":"2");
            prescriptionTpl.setOrgCode(userInfo.getOrgCode());
            prescriptionTpl.setPinYin(Optional.ofNullable(mo.getTplName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            prescriptionTpl.setCreateAt(LocalDateTime.now().toString());
            prescriptionTpl.setCreateBy(userInfo.getId().toString());
            prescriptionTpl.setIsValid("1");
            prescriptionTpl.setPinYin(Optional.ofNullable(mo.getTplName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            prescriptionTplMapper.insert(prescriptionTpl);

        }else{
            prescriptionTpl = prescriptionTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,prescriptionTpl);
            prescriptionTpl.setPinYin(Optional.ofNullable(mo.getTplName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
            prescriptionTpl.setModifyBy(userInfo.getId().toString());
            prescriptionTplMapper.updateByPrimaryKey(prescriptionTpl);

            prescriptionTplItemMapper.deleteByTplId(prescriptionTpl.getId());
            inspectTplMapper.deleteByTplId(prescriptionTpl.getId());
        }

        PrescriptionTpl finalPrescriptionTpl = prescriptionTpl;
        if(null != mo.getItemList() && 0 != mo.getItemList().size()) {
            mo.getItemList().forEach((obj) -> {
                PrescriptionTplItem item = new PrescriptionTplItem();
                BeanUtils.copyProperties(obj, item);
                item.setTplId(finalPrescriptionTpl.getId());
                Drug drug = Optional.ofNullable(obj.getDrugId()).
                        map(id -> drugMapper.selectByPrimaryKey(id)).orElse(null);
                if(null != drug){
                    item.setDrugName(drug.getName());
                    item.setDrugCategory(drug.getCategory());
                    item.setDrugCode(drug.getDrugCode());
                }

                prescriptionTplItemMapper.insertItem(item, finalPrescriptionTpl);
            });
        }

        if(null != mo.getInspectList() && 0 != mo.getInspectList().size()) {
            mo.getInspectList().forEach(obj -> {
                InspectTpl inspectTpl = new InspectTpl();
                BeanUtils.copyProperties(obj, inspectTpl);
                inspectTpl.setTplId(finalPrescriptionTpl.getId());
                inspectTplMapper.insertItem(inspectTpl, finalPrescriptionTpl);
            });
        }

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

    /**
     * 获取处方模板详情列表
     * @param tplId 处方模板主键
     * @return
     */
    public List<InspectTpl> getInspectTplList(Integer tplId){
        Example example = new Example(InspectTpl.class);
        example.createCriteria().andEqualTo("tplId",tplId);
        return inspectTplMapper.selectByExample(example);
    }


}
