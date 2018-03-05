package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.AdviceTplDto;
import com.acmed.his.model.dto.DiagnosisTplDto;
import com.acmed.his.model.dto.PrescriptionTplDto;
import com.acmed.his.pojo.mo.*;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.PageHelper;
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
     *
     * @param query
     * @param pageSize
     */
    public List<DiagnosisTplDto> getDiagnosisTplList(TplQueryMo query, Integer pageNum, Integer pageSize , UserInfo userInfo){
        PageHelper.startPage(pageNum,pageSize);

        return diagnosisTplMapper.getDiagnosisTplList(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode());
    }

    public Integer getDiagnosisTplTotal(TplQueryMo query, UserInfo userInfo){
        return diagnosisTplMapper.getDiagnosisTplTotal(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode());
    }


    /**
     * 删除诊断模板
     * @param id 诊断模板主键
     * @return
     */
    public void delDiagnosisTpl(Integer id, UserInfo userInfo){
        DiagnosisTpl diagnosisTpl = diagnosisTplMapper.selectByPrimaryKey(id);
        diagnosisTpl.setRemoved("1");
        diagnosisTpl.setModifyAt(LocalDateTime.now().toString());
        diagnosisTpl.setModifyBy(userInfo.getId().toString());
        diagnosisTplMapper.updateByPrimaryKey(diagnosisTpl);
    }

    public void switchDiagnosisTpl(Integer id, UserInfo userInfo) {
        DiagnosisTpl diagnosisTpl = diagnosisTplMapper.selectByPrimaryKey(id);
        diagnosisTpl.setIsValid(diagnosisTpl.getIsValid().equals("1")?"0":"1");
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
            diagnosisTpl.setRemoved("0");
            diagnosisTplMapper.insert(diagnosisTpl);
        }else{
            DiagnosisTpl diagnosisTpl = diagnosisTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,diagnosisTpl);
            diagnosisTpl.setModifyAt(LocalDateTime.now().toString());
            diagnosisTpl.setModifyBy(userInfo.getId().toString());
            diagnosisTplMapper.updateByPrimaryKey(diagnosisTpl);
        }
    }

    /**
     * 获取医嘱模板列表
     * @return
     */
    public List<AdviceTplDto> getAdviceTplList(TplQueryMo query, Integer pageNum, Integer pageSize , UserInfo userInfo){
        PageHelper.startPage(pageNum,pageSize);

        return adviceTplMapper.getAdviceTplList(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode());
    }

    public Integer getAdviceTplTotal(TplQueryMo query, UserInfo userInfo){
        return adviceTplMapper.getAdviceTplTotal(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode());
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
        adviceTpl.setRemoved("1");
        adviceTplMapper.updateByPrimaryKey(adviceTpl);
    }

    public void switchAdviceTpl(Integer id,UserInfo userInfo){
        AdviceTpl adviceTpl = adviceTplMapper.selectByPrimaryKey(id);
        adviceTpl.setModifyAt(LocalDateTime.now().toString());
        adviceTpl.setModifyBy(userInfo.getId().toString());
        adviceTpl.setIsValid(adviceTpl.getIsValid().equals("1")?"0":"1");
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
            adviceTpl.setRemoved("0");
            adviceTplMapper.insert(adviceTpl);
        }else{
            AdviceTpl adviceTpl = adviceTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,adviceTpl);
            adviceTpl.setModifyAt(LocalDateTime.now().toString());
            adviceTpl.setModifyBy(userInfo.getId().toString());
            adviceTplMapper.updateByPrimaryKey(adviceTpl);
        }
    }

    /**
     * 获取处方模板列表
     * @param
     * @return
     */
    public List<PrescriptionTplDto> getPrescripTplList(PrescriptionQueryTplMo query, Integer pageNum, Integer pageSize, UserInfo userInfo){
        PageHelper.startPage(pageNum,pageSize);
        return prescriptionTplMapper.getPrescripTplList(Optional.ofNullable(query).map(obj->obj.getTplName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getCategory()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo, DicTypeEnum.PRESCRIPTION.getCode());
    }

    public Integer getPrescripTplTotal( PrescriptionQueryTplMo query, UserInfo userInfo) {
        return prescriptionTplMapper.getPrescripTplTotal(Optional.ofNullable(query).map(obj->obj.getTplName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getCategory()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo);
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
    public void switchPrescripTpl(Integer id,UserInfo userInfo){
        PrescriptionTpl prescriptionTpl = prescriptionTplMapper.selectByPrimaryKey(id);
        prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
        prescriptionTpl.setModifyBy(userInfo.getId().toString());
        prescriptionTpl.setIsValid(prescriptionTpl.getIsValid().equals("1")?"0":"1");
        prescriptionTplMapper.updateByPrimaryKey(prescriptionTpl);
    }

    /**
     * 删除处方模板
      * @param id
     * @param userInfo
     */
    @Transactional
    public void delPrescripTpl(Integer id,UserInfo userInfo){
        PrescriptionTpl prescriptionTpl = prescriptionTplMapper.selectByPrimaryKey(id);
        prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
        prescriptionTpl.setModifyBy(userInfo.getId().toString());
        prescriptionTpl.setRemoved("1");
        prescriptionTplMapper.updateByPrimaryKey(prescriptionTpl);
    }

    /**
     * 新增/编辑 处方模板
     * @param mo 处方模板
     * @return
     */
    @Transactional
    public Integer savePrescripTpl(PrescriptionTplMo mo, UserInfo userInfo){
        PrescriptionTpl prescriptionTpl = null;
        if(null == mo.getId()){
            prescriptionTpl = new PrescriptionTpl();
            BeanUtils.copyProperties(mo,prescriptionTpl);
            prescriptionTpl.setOrgCode(userInfo.getOrgCode());
            prescriptionTpl.setPinYin(Optional.ofNullable(mo.getTplName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            prescriptionTpl.setCreateAt(LocalDateTime.now().toString());
            prescriptionTpl.setCreateBy(userInfo.getId().toString());
            prescriptionTpl.setIsValid("1");
            prescriptionTpl.setRemoved("0");
            prescriptionTplMapper.insert(prescriptionTpl);

        }else{
            prescriptionTpl = prescriptionTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,prescriptionTpl);
            prescriptionTpl.setPinYin(Optional.ofNullable(mo.getTplName()).map(PinYinUtil::getPinYinHeadChar).orElse(null));
            prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
            prescriptionTpl.setModifyBy(userInfo.getId().toString());
            prescriptionTplMapper.updateByPrimaryKey(prescriptionTpl);


        }
        return prescriptionTpl.getId();
    }

    @Transactional
    public  boolean savePrescripItemTpl(PrescriptionTplItemMo mo) {

        prescriptionTplItemMapper.deleteByTplId(mo.getTplId());
        if(null != mo.getList() && 0 != mo.getList().size()) {
            mo.getList().forEach((obj) -> {
                PrescriptionTplItem item = new PrescriptionTplItem();
                BeanUtils.copyProperties(obj, item);
                item.setTplId(mo.getTplId());
                Drug drug =drugMapper.selectByPrimaryKey(obj.getDrugId());
                if(null != drug){
                    item.setDrugCode(drug.getDrugCode());
                }
                prescriptionTplItemMapper.insert(item);
            });
        }
        return true;
    }

    @Transactional
    public boolean savInspectTpl(InspectTplMo mo) {
        inspectTplMapper.deleteByTplId(mo.getTplId());
        if(null != mo.getList() && 0 != mo.getList().size()) {
            mo.getList().forEach(obj -> {
                InspectTpl inspectTpl = new InspectTpl();
                BeanUtils.copyProperties(obj, inspectTpl);
                inspectTpl.setTplId(mo.getTplId());
                inspectTplMapper.insert(inspectTpl);
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
