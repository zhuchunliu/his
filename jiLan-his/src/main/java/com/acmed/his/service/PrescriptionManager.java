package com.acmed.his.service;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.*;
import com.acmed.his.model.*;
import com.acmed.his.pojo.mo.PreMo;
import com.acmed.his.pojo.vo.PreVo;
import com.acmed.his.pojo.vo.PrescriptionVo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.DateTimeUtil;
import com.acmed.his.util.UUIDUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.*;

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
    private PatientItemManager patientItemManager;

    @Autowired
    private MedicalRecordMapper recordMapper;

    @Autowired
    private PrescriptionFeeMapper feeMapper;

    @Autowired
    private BaseInfoManager baseInfoManager;


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

        List<DicItem> dicItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_FREQUENCY.getCode());
        Map<String,String> dicItemName = Maps.newHashMap();
        dicItemList.forEach(obj->{
            dicItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });
        return new PreVo(prescription,preInspectList,chargeList,preItemList,patient,medicalRecord,dicItemName);
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
            feeMapper.delByPreId(prescription.getId());
        }

        Double price = 0d;
        for(int i=0; i< mo.getPreList().size(); i++){
            PreMo.PrescriptMo pre = mo.getPreList().get(i);
            Double childPrice = 0.0d;
            if(null != pre.getItemList()) {
                for (PreMo.ItemMo info : pre.getItemList()) {
                    Drug drug = drugMapper.selectByPrimaryKey(info.getDrugId());
                    PrescriptionItem item = new PrescriptionItem();
                    BeanUtils.copyProperties(info,item,"id");
                    item.setId(UUIDUtil.generate());
                    item.setDrugName(Optional.ofNullable(drug.getGoodsName()).orElse(drug.getName()));
                    item.setCategory(drug.getCategory());
                    item.setPrescriptionId(prescription.getId());
                    item.setApplyId(apply.getId());
                    item.setDrugId(info.getDrugId());
                    item.setDrugCode(drug.getDrugCode());
                    item.setPayStatus(0);
                    item.setBid(Optional.ofNullable(drug.getBid()).orElse(0d));//存当前进价
                    item.setRetailPrice(Optional.ofNullable(drug.getRetailPrice()).orElse(0d));//存当前零售价
                    item.setFee(item.getNum() * item.getRetailPrice());//总价：单价*数量
                    item.setGroupNum(String.valueOf(i+1));
                    item.setRequirement(pre.getRequirement());
                    item.setRemark(pre.getRemark());
                    preItemMapper.insert(item);
                    childPrice += item.getFee();
                }
            }

            if(null != pre.getInspectList()) {
                for (PreMo.InspectMo info : pre.getInspectList()) {
                    Inspect inspect = new Inspect();
                    BeanUtils.copyProperties(prescription,inspect,"id");
                    BeanUtils.copyProperties(info, inspect);
                    inspect.setId(UUIDUtil.generate());
                    inspect.setPrescriptionId(prescription.getId());
                    inspect.setApplyId(apply.getId());
                    inspect.setPayStatus(0);
                    inspect.setGroupNum(String.valueOf(i+1));
                    inspect.setDept(Optional.ofNullable(prescription.getDept()).map(obj->obj.toString()).orElse(null));
                    inspect.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.INSPECT_CATEGORY.getCode(),inspect.getCategory())).
                            map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                    inspect.setRequirement(pre.getRequirement());
                    inspect.setRemark(pre.getRemark());
                    inspectMapper.insert(inspect);
                    childPrice += inspect.getFee();
                }
            }

            if(null != pre.getChargeList()) {
                for (PreMo.ChargeMo info : pre.getChargeList()) {
                    Charge charge = new Charge();
                    BeanUtils.copyProperties(prescription,charge,"id");
                    charge.setId(UUIDUtil.generate());
                    charge.setApplyId(apply.getId());
                    charge.setPrescriptionId(prescription.getId());
                    charge.setCategory(info.getCategory());
                    charge.setPayStatus(0);
                    charge.setGroupNum(String.valueOf(i+1));
                    charge.setFee(Optional.ofNullable(feeItemManager.getFeeItemDetail(userInfo.getOrgCode(),DicTypeEnum.CHARGE_CATEGORY.getCode(),charge.getCategory())).
                            map(obj->Double.parseDouble(obj.getItemPrice().toString())).orElse(0d));
                    charge.setRequirement(pre.getRequirement());
                    charge.setRemark(pre.getRemark());
                    chargeMapper.insert(charge);
                    childPrice += charge.getFee();
                }
            }

            PrescriptionFee prescriptionFee = new PrescriptionFee();
            prescriptionFee.setApplyId(apply.getId());
            prescriptionFee.setPrescriptionId(prescription.getId());
            prescriptionFee.setReceivables(childPrice);
            prescriptionFee.setGroupNum(String.valueOf(i+1));
            prescriptionFee.setCreateAt(LocalDateTime.now().toString());
            prescriptionFee.setCreateBy(userInfo.getId().toString());
            feeMapper.insert(prescriptionFee);

            price += childPrice;


        }


        prescription.setFee(price);
        preMapper.updateByPrimaryKey(prescription);
    }


    /**
     * 处理患者信息
     * @return
     */
    private Patient handlePatient(PreMo mo,UserInfo userInfo){
        Patient patient = patientManager.getPatientByIdCard(mo.getPatient().getIdCard());
        if(null == patient){
            patient = new Patient();
            BeanUtils.copyProperties(mo.getPatient(),patient);
            patient = patientManager.add(patient);
        }

        PatientItem patientItem = patientItemManager.getPatientByIdCard(mo.getPatient().getIdCard(),userInfo.getOrgCode());
        if(null == patientItem){
            patientItem = new PatientItem();
            BeanUtils.copyProperties(mo.getPatient(),patientItem);
            patientItem.setOrgCode(userInfo.getOrgCode());
            patientItem.setPatientId(patient.getId());
            patientItemManager.addPatinetItem(patientItem);
        }else{
            BeanUtils.copyProperties(mo.getPatient(),patientItem);
            patientItem.setPatientName(mo.getPatient().getRealName());
            patientItem.setOrgCode(userInfo.getOrgCode());
            patientItem.setPatientId(patient.getId());
            patientItemManager.updatePatientItem(patientItem);
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
            apply.setPatientName(patient.getRealName());
            apply.setGender(patient.getGender());
            apply.setAge(Optional.ofNullable(patient.getDateOfBirth()).map(DateTimeUtil::getAge).orElse(null));
            apply.setStatus("1");
            apply.setClinicNo(commonManager.getFormatVal(userInfo.getOrgCode() + "applyCode", "000000000"));
            apply.setFee(userInfo.getApplyfee());//设置成用户配置的挂号费
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

    /**
     * 获取实时处方销售额
     * @param orgCode
     * @return
     */
    public Double getCurrentDayItemFee(Integer orgCode) {
        return preItemMapper.getCurrentDayItemFee(orgCode);
    }

    /**
     * 获取收支概况
     *
     * @param startTime
     * @param endTime
     * @param orgCode
     * @return
     */
    public Double getSurveyFee(Integer orgCode,String startTime, String endTime) {
        return preItemMapper.getSurveyFee(orgCode,startTime,endTime);
    }

    /**
     * 根据applyId获取处方信息
     * @param applyId
     * @return
     */
    public List<PrescriptionVo> getPreByApplyId(String applyId) {

        Map<String,PrescriptionVo> map = new TreeMap<>();

        Example example = new Example(PrescriptionItem.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("id").asc();
        List<PrescriptionItem> preItemList = preItemMapper.selectByExample(example);

        if(null != preItemList && 0 != preItemList.size()){
            preItemList.forEach(obj->{
                PreVo.ItemVo item = new PreVo().new ItemVo();
                BeanUtils.copyProperties(obj,item);
                item.setTotalFee(Optional.ofNullable(item.getNum()).orElse(0)*Optional.ofNullable(item.getFee()).orElse(0d));

                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new PrescriptionVo("1",item,null,null));
                }else{
                    map.get(obj.getGroupNum()).getItemList().add(item);
                    map.get(obj.getGroupNum()).setType("1");
                }
            });

        }

        example = new Example(Inspect.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("id").asc();
        List<Inspect> preInspectList = inspectMapper.selectByExample(example);

        if(null != preInspectList && 0 != preInspectList.size()){
            preInspectList.forEach((obj)->{
                PreVo.InspectVo inspect = new PreVo().new InspectVo();
                BeanUtils.copyProperties(obj,inspect);

                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new PrescriptionVo("2",null,inspect,null));
                }else{
                    map.get(obj.getGroupNum()).getInspectList().add(inspect);
                    map.get(obj.getGroupNum()).setType("2");
                }
            });

        }

        example = new Example(Charge.class);
        example.createCriteria().andEqualTo("applyId",applyId);
        example.orderBy("id").asc();
        List<Charge> chargeList = chargeMapper.selectByExample(example);

        if(null != chargeList && 0 != chargeList.size()){
            chargeList.forEach((obj)->{
                PreVo.ChargeVo charge = new PreVo().new ChargeVo();
                BeanUtils.copyProperties(obj,charge);

                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new PrescriptionVo(null,null,null,charge));
                }else{
                    map.get(obj.getGroupNum()).getChargeList().add(charge);
                }
            });

        }
        List<PrescriptionVo> list = Lists.newArrayList();
        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            list.add(map.get(iterator.next()));
        }

        return list;
    }
}
