package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.model.dto.PreTitleDto;
import com.acmed.his.pojo.mo.PreInspectMo;
import com.acmed.his.pojo.mo.PreMedicineMo;
import com.acmed.his.pojo.mo.PreMo;
import com.acmed.his.pojo.vo.PreInspectVo;
import com.acmed.his.pojo.vo.PreMedicineVo;
import com.acmed.his.pojo.vo.PreVo;
import com.acmed.his.pojo.vo.UserInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-22
 **/
@Service
public class PrescriptionManager {

    private Logger logger = LoggerFactory.getLogger(PrescriptionManager.class);

    @Autowired
    private PrescriptionMapper preMapper;

    @Autowired
    private PrescriptionItemMapper preItemMapper;

    @Autowired
    private InspectMapper inspectMapper;

    @Autowired
    private ChargeMapper chargeMapper;

    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private DrugMapper drugMapper;

    @Autowired
    private FeeItemManager feeItemManager;

    @Autowired
    private CommonManager commonManager;

    /**
     * 开药品处方
     * @param mo
     */
    @Transactional
    public boolean savePreMedicine(PreMedicineMo mo, UserInfo userInfo) {
        Apply apply = applyMapper.selectByPrimaryKey(mo.getApplyId());
        if(null == apply){
            logger.error("保存药品处方失败，找不到挂号单： "+mo.getApplyId());
            return false;
        }
        Prescription prescription = Optional.ofNullable(mo.getId()).map(id->preMapper.selectByPrimaryKey(id)).orElse(new Prescription());
        prescription.setApplyId(mo.getApplyId());
        prescription.setPatientId(apply.getPatientId());
        prescription.setOrgCode(apply.getOrgCode());
        prescription.setDept(apply.getDept());
        prescription.setDeptName(apply.getDeptName());
        prescription.setCategory("1");
        prescription.setIsPaid("0");

        if(null == mo.getId()){
            prescription.setPrescriptionNo(apply.getId()+commonManager.getNextVal("Org"+apply.getId()));
            prescription.setCreateAt(LocalDateTime.now().toString());
            prescription.setCreateBy(userInfo.getId().toString());
            preMapper.insert(prescription);
            prescription = preMapper.getByNo(prescription.getPrescriptionNo());
        }else{
            prescription.setModifyAt(LocalDateTime.now().toString());
            prescription.setModifyBy(userInfo.getId().toString());
            preItemMapper.delByPreId(prescription.getId());
            chargeMapper.delByPreId(prescription.getId());
        }

        Double price = 0d;
        if(null != mo.getItemList()) {
            for (PreMedicineMo.Item info : mo.getItemList()) {
                Drug drug = drugMapper.selectByPrimaryKey(info.getDrugId());
                PrescriptionItem item = new PrescriptionItem();
                BeanUtils.copyProperties(info,item,"id");
                item.setDrugName(drug.getName());
                item.setCategory(drug.getCategory());
                item.setPrescriptionId(prescription.getId());
                item.setApplyId(mo.getApplyId());
                item.setDrugId(info.getDrugId());
                item.setDrugCode(drug.getDrgCode());
                item.setFee(Optional.ofNullable(drug.getRetailPrice()).orElse(0d));//总价：单价*数量
                preItemMapper.insertItem(item,prescription.getPrescriptionNo());
                price += info.getNum() * item.getFee();
            }
        }

        if(null != mo.getChargeList()) {
            for (PreMedicineMo.Charge info : mo.getChargeList()) {
                Charge charge = new Charge();
                BeanUtils.copyProperties(prescription,charge,"id");
                charge.setCategory(info.getCategory());
                charge.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.CHARGE_CATEGORY.getCode(),charge.getCategory())).
                                map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                price += charge.getFee();
                chargeMapper.insertCharge(charge,prescription.getPrescriptionNo());
            }
        }
        prescription.setFee(price);
        preMapper.updateByPrimaryKey(prescription);

        applyMapper.statisFee(apply.getId());//统计总费用
        return true;
    }


    @Transactional
    public boolean savePreInspect(PreInspectMo mo,UserInfo userInfo) {
        Apply apply = applyMapper.selectByPrimaryKey(mo.getApplyId());
        if(null == apply){
            logger.error("保存药品处方失败，找不到挂号单： "+mo.getApplyId());
            return false;
        }
        Prescription prescription = Optional.ofNullable(mo.getId()).map(id->preMapper.selectByPrimaryKey(id)).orElse(new Prescription());
        prescription.setApplyId(mo.getApplyId());
        prescription.setPatientId(apply.getPatientId());
        prescription.setOrgCode(apply.getOrgCode());
        prescription.setDept(apply.getDept());
        prescription.setDeptName(apply.getDeptName());
        prescription.setCategory("1");
        prescription.setIsPaid("0");

        if(null == mo.getId()){
            prescription.setPrescriptionNo(apply.getId()+commonManager.getNextVal("Org"+apply.getId()));
            prescription.setCreateAt(LocalDateTime.now().toString());
            prescription.setCreateBy(userInfo.getId().toString());
            preMapper.insert(prescription);
            prescription = preMapper.getByNo(prescription.getPrescriptionNo());
        }else{
            prescription.setModifyAt(LocalDateTime.now().toString());
            prescription.setModifyBy(userInfo.getId().toString());
            inspectMapper.delByPreId(prescription.getId());
            chargeMapper.delByPreId(prescription.getId());
        }


        double price = 0d;
        if(null != mo.getInspectList()) {
            for (PreInspectMo.Inspect info : mo.getInspectList()) {
                Inspect inspect = new Inspect();
                BeanUtils.copyProperties(prescription,inspect,"id");
                BeanUtils.copyProperties(info, inspect);
                inspect.setApplyId(mo.getApplyId());
                inspect.setDept(Optional.ofNullable(prescription.getDept()).map(obj->obj.toString()).orElse(null));
                inspect.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.INSPECT_CATEGORY.getCode(),inspect.getCategory())).
                        map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                inspectMapper.insertInspect(inspect,prescription.getPrescriptionNo());
                price += inspect.getFee();
            }
        }

        if(null != mo.getChargeList()) {
            for (PreInspectMo.Charge info : mo.getChargeList()) {
                Charge charge = new Charge();
                BeanUtils.copyProperties(prescription,charge,"id");
                charge.setPrescriptionId(prescription.getId());
                charge.setCategory(info.getCategory());
                charge.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.CHARGE_CATEGORY.getCode(),charge.getCategory())).
                        map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                price += charge.getFee();
                chargeMapper.insertCharge(charge,prescription.getPrescriptionNo());
            }
        }



        prescription.setFee(price);
        preMapper.updateByPrimaryKey(prescription);

        applyMapper.statisFee(apply.getId());//统计总费用
        return true;
    }

    public List<PreTitleDto> getPreByApply(Integer applyId) {
        return preMapper.getPreByApply(applyId);
    }

    /**
     * 获取用药处方
     * @param id
     * @return
     */
    public PreMedicineVo getPreMedicine(Integer id) {

        Example example = new Example(Charge.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<PrescriptionItem> preItemList = preItemMapper.selectByExample(example);

        Prescription prescription = preMapper.selectByPrimaryKey(id);
        return new PreMedicineVo(prescription,preItemList,chargeList);
    }

    /**
     * 获取检查处方
     * @param id
     * @return
     */
    public PreInspectVo getPreInspect(Integer id) {
        Example example = new Example(Charge.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Inspect> preItemList = inspectMapper.selectByExample(example);

        Prescription prescription = preMapper.selectByPrimaryKey(id);
        return new PreInspectVo(prescription,preItemList,chargeList);
    }

    /**
     * 获取处方
     * @param id
     * @return
     */
    public PreVo getPre(Integer id) {
        Example example = new Example(Charge.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<Inspect> preInspectList = inspectMapper.selectByExample(example);

        Prescription prescription = preMapper.selectByPrimaryKey(id);

        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<PrescriptionItem> preItemList = preItemMapper.selectByExample(example);
        return new PreVo(prescription,preInspectList,chargeList,preItemList);
    }

    /**
     * 一次性保存处方信息
     * @param mo
     * @param userInfo
     * @return
     */
    @Transactional
    public boolean savePre(PreMo mo, UserInfo userInfo) {

        Apply apply = applyMapper.selectByPrimaryKey(mo.getApplyId());
        if(null == apply){
            logger.error("保存药品处方失败，找不到挂号单： "+mo.getApplyId());
            return false;
        }
        Prescription prescription = Optional.ofNullable(mo.getId()).map(id->preMapper.selectByPrimaryKey(id)).orElse(new Prescription());
        prescription.setApplyId(mo.getApplyId());
        prescription.setPatientId(apply.getPatientId());
        prescription.setOrgCode(apply.getOrgCode());
        prescription.setDept(apply.getDept());
        prescription.setDeptName(apply.getDeptName());
        prescription.setCategory("1");
        prescription.setIsPaid("0");

        if(null == mo.getId()){
            prescription.setPrescriptionNo(apply.getId()+commonManager.getNextVal("Org"+apply.getId()));
            prescription.setCreateAt(LocalDateTime.now().toString());
            prescription.setCreateBy(userInfo.getId().toString());
            preMapper.insert(prescription);
            prescription = preMapper.getByNo(prescription.getPrescriptionNo());
        }else{
            prescription.setModifyAt(LocalDateTime.now().toString());
            prescription.setModifyBy(userInfo.getId().toString());
            preItemMapper.delByPreId(prescription.getId());
            chargeMapper.delByPreId(prescription.getId());
            inspectMapper.delByPreId(prescription.getId());
        }

        Double price = 0d;
        if(null != mo.getItemList()) {
            for (PreMo.Item info : mo.getItemList()) {
                Drug drug = drugMapper.selectByPrimaryKey(info.getDrugId());
                PrescriptionItem item = new PrescriptionItem();
                BeanUtils.copyProperties(info,item,"id");
                item.setDrugName(drug.getName());
                item.setCategory(drug.getCategory());
                item.setPrescriptionId(prescription.getId());
                item.setApplyId(mo.getApplyId());
                item.setDrugId(info.getDrugId());
                item.setDrugCode(drug.getDrgCode());
                item.setFee(Optional.ofNullable(drug.getRetailPrice()).orElse(0d));//总价：单价*数量
                preItemMapper.insertItem(item,prescription.getPrescriptionNo());
                price += info.getNum() * item.getFee();
            }
        }

        if(null != mo.getInspectList()) {
            for (PreMo.Inspect info : mo.getInspectList()) {
                Inspect inspect = new Inspect();
                BeanUtils.copyProperties(prescription,inspect,"id");
                BeanUtils.copyProperties(info, inspect);
                inspect.setApplyId(mo.getApplyId());
                inspect.setDept(Optional.ofNullable(prescription.getDept()).map(obj->obj.toString()).orElse(null));
                inspect.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.INSPECT_CATEGORY.getCode(),inspect.getCategory())).
                        map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                inspectMapper.insertInspect(inspect,prescription.getPrescriptionNo());
                price += inspect.getFee();
            }
        }

        if(null != mo.getChargeList()) {
            for (PreMo.Charge info : mo.getChargeList()) {
                Charge charge = new Charge();
                BeanUtils.copyProperties(prescription,charge,"id");
                charge.setCategory(info.getCategory());
                charge.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.CHARGE_CATEGORY.getCode(),charge.getCategory())).
                        map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                price += charge.getFee();
                chargeMapper.insertCharge(charge,prescription.getPrescriptionNo());
            }
        }
        prescription.setFee(price);
        preMapper.updateByPrimaryKey(prescription);

        applyMapper.statisFee(apply.getId());//统计总费用
        return true;
    }


}
