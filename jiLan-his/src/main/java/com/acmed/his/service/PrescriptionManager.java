package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.pojo.mo.PreMo;
import com.acmed.his.pojo.vo.PreVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.DateTimeUtil;
import com.acmed.his.util.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
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
    private DrugMapper drugMapper;

    @Autowired
    private FeeItemManager feeItemManager;

    @Autowired
    private CommonManager commonManager;

    @Autowired
    private ApplyManager applyManager;

    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private PatientManager patientManager;

    @Autowired
    private MedicalRecordMapper recordMapper;


    /**
     * 根据挂号单查找处方列表
     * @param applyId
     * @return
     */
    public List<Prescription> getPreByApply(String applyId) {
        return preMapper.getPreByApply(applyId);
    }

    /**
     * 获取处方
     * @param id
     * @return
     */
    public PreVo getPre(String id) {
        Example example = new Example(Charge.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        example.orderBy("id").asc();
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        example.orderBy("id").asc();
        List<Inspect> preInspectList = inspectMapper.selectByExample(example);

        Prescription prescription = preMapper.selectByPrimaryKey(id);

        example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        example.orderBy("id").asc();
        List<PrescriptionItem> preItemList = preItemMapper.selectByExample(example);

        Patient patient = patientManager.getPatientById(prescription.getPatientId());

        example = new Example(MedicalRecord.class);
        example.createCriteria().andEqualTo("patientId",prescription.getPatientId());
        MedicalRecord medicalRecord = Optional.ofNullable(recordMapper.selectByExample(example)).
                filter(obj->0!=obj.size()).map(obj->obj.get(0)).orElse(new MedicalRecord());


        return new PreVo(prescription,preInspectList,chargeList,preItemList,patient,medicalRecord);
    }

    /**
     * 一次性保存处方信息
     * @param mo
     * @param userInfo
     * @return
     */
    @Transactional
    public boolean savePre(PreMo mo, UserInfo userInfo) {
        Apply apply = null;
        if(StringUtils.isEmpty(mo.getId())) {
            //step0：
            if(!StringUtils.isEmpty(mo.getApplyId())){//如果根据挂号单开处方，不允许换患者
                mo.getPatient().setPatientId(applyMapper.selectByPrimaryKey(mo.getApplyId()).getPatientId());
            }

            //step1:处理患者信息
            Patient patient = this.handlePatient(mo, userInfo);

            //step2:处理挂号信息
            apply = this.handleApply(mo, patient, userInfo);

        }else{//编辑不用处理患者和挂号信息，直接获取挂号信息
            if(StringUtils.isEmpty(mo.getApplyId())){//前端未传挂号主键
                mo.setApplyId(preMapper.selectByPrimaryKey(mo.getId()).getApplyId());
            }
            apply = applyMapper.selectByPrimaryKey(mo.getApplyId());
        }

        //step3:处理就诊信息
        this.handleMedicalRecord(mo,apply,userInfo);

        //step4:保存处方信息
        this.handlePrescription(mo,apply,userInfo);

        //step5：统计处方总费用
        applyMapper.statisFee(apply.getId());//统计总费用
        return true;
    }


    /**
     * 确认发药
     * @param id
     * @param userInfo
     */
    @Transactional
    public void dispensing(String id, UserInfo userInfo) {
        Prescription prescription = preMapper.selectByPrimaryKey(id);
        prescription.setIsDispensing("1");
        prescription.setModifyBy(userInfo.getId().toString());
        prescription.setModifyAt(LocalDateTime.now().toString());
        preMapper.updateByPrimaryKey(prescription);

        //扣除库存
        Example example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("prescriptionId",id);
        List<PrescriptionItem> list = preItemMapper.selectByExample(example);
        list.forEach(obj->{
            Drug drug = drugMapper.selectByPrimaryKey(obj.getDrugId());
            drug.setNum(drug.getNum()-obj.getNum());
            drugMapper.updateByPrimaryKey(drug);
        });
    }

    /**
     * 处理处方信息
     */
    private void handlePrescription(PreMo mo, Apply apply, UserInfo userInfo){

        Prescription prescription = Optional.ofNullable(mo.getId()).map(id->preMapper.selectByPrimaryKey(mo.getId()))
                .orElse(new Prescription());

        if(StringUtils.isEmpty(mo.getId())){//新增
            BeanUtils.copyProperties(apply,prescription);
            prescription.setId(UUIDUtil.generate());
            prescription.setApplyId(apply.getId());
            prescription.setPrescriptionNo(apply.getId()+commonManager.getNextVal("Org"+apply.getId()));
            prescription.setIsPaid("0");
            prescription.setIsDispensing("0");
            prescription.setCreateAt(LocalDateTime.now().toString());
            prescription.setCreateBy(userInfo.getId().toString());
            preMapper.insert(prescription);
        }else{
            prescription.setModifyAt(LocalDateTime.now().toString());
            prescription.setModifyBy(userInfo.getId().toString());
            preItemMapper.delByPreId(prescription.getId());
            chargeMapper.delByPreId(prescription.getId());
            inspectMapper.delByPreId(prescription.getId());
        }

        Double price = 0d;
        for(int i=0; i< mo.getPreList().size(); i++){
            PreMo.PrescriptMo pre = mo.getPreList().get(i);

            if(null != pre.getItemList()) {
                for (PreMo.ItemMo info : pre.getItemList()) {
                    Drug drug = drugMapper.selectByPrimaryKey(info.getDrugId());
                    PrescriptionItem item = new PrescriptionItem();
                    BeanUtils.copyProperties(info,item,"id");
                    item.setId(UUIDUtil.generate());
                    item.setDrugName(drug.getName());
                    item.setCategory(drug.getCategory());
                    item.setPrescriptionId(prescription.getId());
                    item.setApplyId(mo.getApplyId());
                    item.setDrugId(info.getDrugId());
                    item.setDrugCode(drug.getDrugCode());
                    item.setBid(Optional.ofNullable(drug.getBid()).orElse(0d));//存当前进价
                    item.setRetailPrice(Optional.ofNullable(drug.getRetailPrice()).orElse(0d));//存当前零售价
                    item.setFee(item.getNum() * item.getRetailPrice());//总价：单价*数量
                    item.setGroupNum(String.valueOf(i+1));
                    preItemMapper.insert(item);
                    price += info.getNum() * item.getFee();
                }
            }

            if(null != pre.getInspectList()) {
                for (PreMo.InspectMo info : pre.getInspectList()) {
                    Inspect inspect = new Inspect();
                    BeanUtils.copyProperties(prescription,inspect,"id");
                    BeanUtils.copyProperties(info, inspect);
                    inspect.setId(UUIDUtil.generate());
                    inspect.setPrescriptionId(prescription.getId());
                    inspect.setApplyId(mo.getApplyId());
                    inspect.setGroupNum(String.valueOf(i+1));
                    inspect.setDept(Optional.ofNullable(prescription.getDept()).map(obj->obj.toString()).orElse(null));
                    inspect.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.INSPECT_CATEGORY.getCode(),inspect.getCategory())).
                            map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                    inspectMapper.insert(inspect);
                    price += inspect.getFee();
                }
            }

            if(null != pre.getChargeList()) {
                for (PreMo.ChargeMo info : pre.getChargeList()) {
                    Charge charge = new Charge();
                    BeanUtils.copyProperties(prescription,charge,"id");
                    charge.setId(UUIDUtil.generate());
                    charge.setPrescriptionId(prescription.getId());
                    charge.setCategory(info.getCategory());
                    charge.setGroupNum(String.valueOf(i+1));
                    charge.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.CHARGE_CATEGORY.getCode(),charge.getCategory())).
                            map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                    price += charge.getFee();
                    chargeMapper.insert(charge);
                }
            }
        }


        prescription.setFee(price);
        preMapper.updateByPrimaryKey(prescription);
    }


    /**
     * 处理患者信息
     * @return
     */
    private Patient handlePatient(PreMo mo,UserInfo userInfo){
        Patient patient = new Patient();
        if(!StringUtils.isEmpty(mo.getPatient().getPatientId())){
            patient = patientManager.getPatientById(mo.getPatient().getPatientId());
            BeanUtils.copyProperties(mo.getPatient(),patient);
            patientManager.update(patient);
        }else{//如果是新用户，则添加用户信息
            BeanUtils.copyProperties(mo.getPatient(),patient);
            patient.setId(UUIDUtil.generate());
            patient.setCreateBy(userInfo.getId().toString());
            patient.setCreateAt(LocalDateTime.now().toString());
            patientManager.add(patient);
        }
        return patient;
    }

    /**
     * 处理挂号单信息
     * @return
     */
    private Apply handleApply(PreMo mo,Patient patient,UserInfo userInfo){
        Apply apply = new Apply();
        if(!StringUtils.isEmpty(mo.getApplyId())){
            apply = applyManager.getApplyById(mo.getApplyId());
        }else{//如果是新挂号单，则添加新挂号单
            apply.setId(UUIDUtil.generate());
            apply.setOrgCode(userInfo.getOrgCode());
            apply.setOrgName(userInfo.getOrgName());
            apply.setDept(userInfo.getDept());
            apply.setDeptName(userInfo.getDeptName());
            apply.setPatientId(patient.getId());
            apply.setPatientName(patient.getUserName());
            apply.setGender(patient.getGender());
            apply.setAge(Optional.ofNullable(patient.getDateOfBirth()).map(DateTimeUtil::getAge).orElse(null));
            apply.setStatus("1");
            apply.setClinicNo(commonManager.getFormatVal(userInfo.getOrgCode() + "applyCode", "000000000"));
            apply.setCreateAt(LocalDateTime.now().toString());
            apply.setCreateBy(userInfo.getId().toString());
            applyMapper.insert(apply);
        }
        return apply;
    }

    /**
     * 处理病例信息
     */
    private void handleMedicalRecord(PreMo mo, Apply apply, UserInfo userInfo) {
       MedicalRecord medicalRecord = Optional.ofNullable(recordMapper.selectByPrimaryKey(apply.getId())).orElse(new MedicalRecord());
       BeanUtils.copyProperties(mo.getRecord(),medicalRecord,"id");
       BeanUtils.copyProperties(apply,medicalRecord,"id");
       if(StringUtils.isEmpty(medicalRecord.getId())){
           medicalRecord.setId(UUIDUtil.generate());
           medicalRecord.setCreateAt(LocalDateTime.now().toString());
           medicalRecord.setCreateBy(userInfo.getId().toString());
           recordMapper.insert(medicalRecord);
       }else{
           medicalRecord.setModifyAt(LocalDateTime.now().toString());
           medicalRecord.setModifyBy(userInfo.getId().toString());
           recordMapper.updateByPrimaryKey(medicalRecord);
       }

    }



}
