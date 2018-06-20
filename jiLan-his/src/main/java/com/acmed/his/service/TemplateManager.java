package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.AdviceTplDto;
import com.acmed.his.model.dto.DiagnosisTplDto;
import com.acmed.his.model.dto.PrescriptionTplDto;
import com.acmed.his.pojo.mo.*;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.PinYinUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
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
    private DrugManager drugManager;

    @Autowired
    private DrugDictMapper drugDictMapper;

    @Autowired
    private InspectTplMapper inspectTplMapper;

    @Autowired
    private ChiefComplaintTplMapper chiefComplaintTplMapper;


    public List<Map<String,Object>> getDiagnosisTplList(UserInfo user) {
        return diagnosisTplMapper.getDiagnosisTplList(user);
    }

    /**
     * 获取诊断模板列表
     *
     * @param query
     * @param pageSize
     */
    public PageResult<DiagnosisTplDto> getDiagnosisPageList(TplQueryMo query, Integer pageNum, Integer pageSize , UserInfo userInfo){
        PageResult result = new PageResult();
        Page page = PageHelper.startPage(pageNum,pageSize);
        result.setData(diagnosisTplMapper.getDiagnosisPageList(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode()));
        result.setTotal(page.getTotal());

        return result;
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
            diagnosisTpl.setModifyAt(LocalDateTime.now().toString());
            diagnosisTpl.setModifyBy(userInfo.getId().toString());
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
    public PageResult<AdviceTplDto> getAdviceTplList(TplQueryMo query, Integer pageNum, Integer pageSize , UserInfo userInfo){
        PageResult result = new PageResult();
        Page page = PageHelper.startPage(pageNum,pageSize);

        result.setData(adviceTplMapper.getAdviceTplList(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode()));
        result.setTotal(page.getTotal());
        return result;
    }

    public Integer getAdviceTplTotal(TplQueryMo query, UserInfo userInfo){
        return adviceTplMapper.getAdviceTplTotal(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode());
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
     * 获取医嘱模板列表
     * @return
     */
    public List<Map<String,Object>> getChiefComplaintTplList(UserInfo userInfo){
        return chiefComplaintTplMapper.getChiefComplaintTplList(userInfo);
    }

    /**
     * 获取医嘱模板列表
     * @return
     */
    public PageResult<AdviceTplDto> getChiefComplaintTplPageList(TplQueryMo query, Integer pageNum, Integer pageSize , UserInfo userInfo){
        PageResult result = new PageResult();
        Page page = PageHelper.startPage(pageNum,pageSize);

        result.setData(chiefComplaintTplMapper.getChiefComplaintTplPageList(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode()));
        result.setTotal(page.getTotal());
        return result;
    }

    public Integer getChiefComplaintDisableNum(TplQueryMo query, UserInfo userInfo){
        return chiefComplaintTplMapper.getChiefComplaintTplTotal(Optional.ofNullable(query).map(obj->obj.getName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo,DicTypeEnum.DIAGNOSIS_TPL.getCode());
    }

    /**
     * 删除医嘱模板
     * @param id 诊断模板主键
     * @return
     */
    public void delChiefComplaintDetail(Integer id,UserInfo userInfo){
        ChiefComplaintTpl chiefComplaintTpl = chiefComplaintTplMapper.selectByPrimaryKey(id);
        chiefComplaintTpl.setModifyAt(LocalDateTime.now().toString());
        chiefComplaintTpl.setModifyBy(userInfo.getId().toString());
        chiefComplaintTpl.setRemoved("1");
        chiefComplaintTplMapper.updateByPrimaryKey(chiefComplaintTpl);
    }

    public void switchChiefComplaintTpl(Integer id,UserInfo userInfo){
        ChiefComplaintTpl chiefComplaintTpl = chiefComplaintTplMapper.selectByPrimaryKey(id);
        chiefComplaintTpl.setModifyAt(LocalDateTime.now().toString());
        chiefComplaintTpl.setModifyBy(userInfo.getId().toString());
        chiefComplaintTpl.setIsValid(chiefComplaintTpl.getIsValid().equals("1")?"0":"1");
        chiefComplaintTplMapper.updateByPrimaryKey(chiefComplaintTpl);
    }



    /**
     * 新增/编辑 医嘱模板
     * @param mo 医嘱模板
     * @return
     */
    public void saveChiefComplaintTpl(ChiefComplaintTplMo mo, UserInfo userInfo){
        if(null == mo.getId()){
            ChiefComplaintTpl chiefComplaintTpl = new ChiefComplaintTpl();
            BeanUtils.copyProperties(mo,chiefComplaintTpl);
            chiefComplaintTpl.setCreateAt(LocalDateTime.now().toString());
            chiefComplaintTpl.setCreateBy(userInfo.getId().toString());
            chiefComplaintTpl.setModifyAt(LocalDateTime.now().toString());
            chiefComplaintTpl.setModifyBy(userInfo.getId().toString());
            chiefComplaintTpl.setOrgCode(userInfo.getOrgCode());
            chiefComplaintTpl.setIsValid("1");
            chiefComplaintTpl.setRemoved("0");
            chiefComplaintTplMapper.insert(chiefComplaintTpl);
        }else{
            ChiefComplaintTpl chiefComplaintTpl = chiefComplaintTplMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,chiefComplaintTpl);
            chiefComplaintTpl.setModifyAt(LocalDateTime.now().toString());
            chiefComplaintTpl.setModifyBy(userInfo.getId().toString());
            chiefComplaintTplMapper.updateByPrimaryKey(chiefComplaintTpl);
        }
    }

    /**
     * 获取处方模板列表
     * @param
     * @return
     */
    public PageResult<PrescriptionTplDto> getPrescripTplList(PrescriptionQueryTplMo query, Integer pageNum, Integer pageSize, UserInfo userInfo){
        PageResult result = new PageResult();
        Page page = PageHelper.startPage(pageNum,pageSize);
        result.setData(prescriptionTplMapper.getPrescripTplList(Optional.ofNullable(query).map(obj->obj.getTplName()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getCategory()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsPublic()).orElse(null),
                Optional.ofNullable(query).map(obj->obj.getIsValid()).orElse(null),
                userInfo, DicTypeEnum.PRESCRIPTION.getCode()));
        result.setTotal(page.getTotal());
        return result;
    }

    public Object getGloablPrescripTplList(String tplName, Integer pageNum, Integer pageSize) {
        PageResult result = new PageResult();
        Page page = PageHelper.startPage(pageNum,pageSize);
        result.setData(prescriptionTplMapper.getGloablPrescripTplList(tplName, "1"));
        result.setTotal(page.getTotal());
        return result;
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
            prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
            prescriptionTpl.setModifyBy(userInfo.getId().toString());
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
            for(int index =0; index < mo.getList().size(); index ++) {
                PrescriptionTplItem item = new PrescriptionTplItem();
                BeanUtils.copyProperties(mo.getList().get(index), item);
                item.setTplId(mo.getTplId());
                item.setSn(index);
                prescriptionTplItemMapper.insert(item);
            }
        }
        return true;
    }

    @Transactional
    public boolean savInspectTpl(InspectTplMo mo) {
        inspectTplMapper.deleteByTplId(mo.getTplId());
        if(null != mo.getList() && 0 != mo.getList().size()) {
            for(int index=0; index < mo.getList().size(); index ++) {
                InspectTpl inspectTpl = new InspectTpl();
                BeanUtils.copyProperties(mo.getList().get(index), inspectTpl);
                inspectTpl.setTplId(mo.getTplId());
                inspectTpl.setSn(index);
                inspectTplMapper.insert(inspectTpl);
            }
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


    /**
     * 导入处方模板
     * @param ids
     * @param user
     */
//    @Transactional
    public void importPrescriptTpl(String ids, UserInfo user) {
        List<Integer> tplIdList = Lists.newArrayList();
        List<String> drugDictIdList = Lists.newArrayList();
        try {
            for (String id : ids.split(",")) {
                PrescriptionTpl tpl = prescriptionTplMapper.selectByPrimaryKey(Integer.parseInt(id));
                List<PrescriptionTplItem> itemList = prescriptionTplItemMapper.getPrescripTplItemList(Integer.parseInt(id));
                PrescriptionTpl prescriptionTpl = new PrescriptionTpl();
                prescriptionTpl.setOrgCode(user.getOrgCode());
                prescriptionTpl.setTplName(tpl.getTplName());
                prescriptionTpl.setCategory(tpl.getCategory());
                prescriptionTpl.setPinYin(tpl.getPinYin());
                prescriptionTpl.setDescription(tpl.getDescription());
//                prescriptionTpl.setCreateBy(tpl.getCreateBy());
                prescriptionTpl.setCreateBy(user.getId().toString());
                prescriptionTpl.setCreateAt(LocalDateTime.now().toString());
                prescriptionTpl.setModifyBy(user.getId().toString());
                prescriptionTpl.setModifyAt(LocalDateTime.now().toString());
                prescriptionTpl.setIsPublic("1");
                prescriptionTpl.setIsValid("1");
                prescriptionTpl.setRemoved("0");

                prescriptionTplMapper.insert(prescriptionTpl);

                tplIdList.add(prescriptionTpl.getId());

                for (PrescriptionTplItem item : itemList) {

                    PrescriptionTplItem tplItem = new PrescriptionTplItem();
                    BeanUtils.copyProperties(item, tplItem, "id", "tplId", "drugId");
                    tplItem.setTplId(prescriptionTpl.getId());
                    List<Drug> drugList = drugMapper.getByDrugDictId(item.getDrugId(), user.getOrgCode());
                    if (null == drugList || 0 == drugList.size()) {
                        drugList = drugManager.saveDrugByDict(new String[]{item.getDrugId().toString()}, user,true);
                        drugDictIdList.add(item.getDrugId());
                    }
                    tplItem.setUnitType(1);
                    tplItem.setDrugId(drugList.get(0).getId().toString());
                    prescriptionTplItemMapper.insert(tplItem);

                }
            }
        }catch (Exception ex){
            Example example = new Example(PrescriptionTpl.class);
            example.createCriteria().andIn("id",tplIdList);
            prescriptionTplMapper.deleteByExample(example);

            example = new Example(PrescriptionTplItem.class);
            example.createCriteria().andIn("tplId",tplIdList);
            prescriptionTplItemMapper.deleteByExample(example);

            example = new Example(Drug.class);
            example.createCriteria().andIn("dictId",drugDictIdList).andEqualTo("orgCode",user.getOrgCode());
            drugMapper.deleteByExample(example);

            throw ex;
        }

    }


}
