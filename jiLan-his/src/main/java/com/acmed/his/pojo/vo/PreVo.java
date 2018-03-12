package com.acmed.his.pojo.vo;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.model.*;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.FeeItemManager;
import com.acmed.his.util.DateTimeUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * Created by Darren on 2017-11-30
 **/
@Data
public class PreVo {

    @ApiModelProperty("处方主键")
    private String id;

    @ApiModelProperty("挂号主键")
    private String applyId;

    @ApiModelProperty("处方集合")
    private List<PrescriptVo> PreList = new ArrayList<>();

    @ApiModelProperty("患者信息")
    private PatientVo patient = new PatientVo();

    @ApiModelProperty("病例")
    private MedicalRecordVo record = new MedicalRecordVo();

    public PreVo(){

    }

    public PreVo(Prescription prescription, List<Inspect> preInspectist,
                 List<Charge> preChargeList, List<PrescriptionItem> preItemList, Patient patientInfo, MedicalRecord medicalRecord,
                 ManufacturerMapper manufacturerMapper, BaseInfoManager baseInfoManager, DrugMapper drugMapper, FeeItemManager feeItemManager) {
        if(null == prescription){
            return;
        }
        this.id = prescription.getId();
        this.applyId = prescription.getApplyId();

        BeanUtils.copyProperties(patientInfo,this.patient);
        this.patient.setAge(Optional.ofNullable(patientInfo.getDateOfBirth()).map(DateTimeUtil::getAge).orElse(null));

        BeanUtils.copyProperties(medicalRecord,record);


        List<DicItem> frequencyItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_FREQUENCY.getCode());
        Map<String,String> frequencyItemName = Maps.newHashMap();
        frequencyItemList.forEach(obj->{
            frequencyItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DicItem> unitItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> unitItemName = Maps.newHashMap();
        unitItemList.forEach(obj->{
            unitItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        Map<String,PrescriptVo> map = new TreeMap<>();


        if(null != preItemList && 0 != preItemList.size()){
            preItemList.forEach(obj->{
                PreVo.ItemVo item = new PreVo.ItemVo();
                BeanUtils.copyProperties(obj,item);
                item.setTotalFee(Optional.ofNullable(obj.getNum()).orElse(0)*Optional.ofNullable(obj.getRetailPrice()).orElse(0d));
                if(null != item.getFrequency()){
                    item.setFrequencyName(frequencyItemName.get(item.getFrequency().toString()));
                }
                Drug drug = drugMapper.selectByPrimaryKey(obj.getDrugId());
                if(null != drug) {
                    item.setDoseUnitName(Optional.ofNullable(drug.getDoseUnit()).map(unit->unitItemName.get(unit.toString())).orElse(""));
                    item.setManufacturerName(Optional.ofNullable(drug.getManufacturer()).
                            map(manu -> manufacturerMapper.selectByPrimaryKey(manu)).map(manu -> manu.getName()).orElse(""));
                    item.setUnitName(Optional.ofNullable(drug.getUnit()).map(unit->unitItemName.get(unit.toString())).orElse(""));
                    if(null != drug.getMinPriceUnitType()) {
                        item.setMinOrDoseUnitName(1 == drug.getMinPriceUnitType() ?
                                (null == drug.getMinUnit() ? "" : unitItemName.get(drug.getMinUnit().toString())) :
                                (null == drug.getDoseUnit() ? "" : unitItemName.get(drug.getDoseUnit().toString())));
                    }
                    item.setMinUnitName(null == drug.getMinUnit() ? "" : unitItemName.get(drug.getMinUnit().toString()));
                    item.setMinPriceUnitType(drug.getMinPriceUnitType());
                    item.setRetailPrice(drug.getRetailPrice());
                    item.setMinRetailPrice(drug.getMinRetailPrice());
                }

                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new PrescriptVo("1",item,null,null
                            ,obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getItemList().add(item);
                    map.get(obj.getGroupNum()).setType("1");
                }
            });

        }

        if(null != preInspectist && 0 != preInspectist.size()){
            preInspectist.forEach((obj)->{
                PreVo.InspectVo inspect = new PreVo.InspectVo();
                BeanUtils.copyProperties(obj,inspect);
                inspect.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.INSPECT_CATEGORY.getCode(),inspect.getCategory()).getDicItemName());
                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new PrescriptVo("2",null,inspect,null
                            ,obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getInspectList().add(inspect);
                    map.get(obj.getGroupNum()).setType("2");
                }
            });

        }

        if(null != preChargeList && 0 != preChargeList.size()){
            preChargeList.forEach((obj)->{
                PreVo.ChargeVo charge = new PreVo.ChargeVo();
                BeanUtils.copyProperties(obj,charge);
                charge.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.CHARGE_CATEGORY.getCode(),charge.getCategory()).getDicItemName());
                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new PrescriptVo(null,null,null,charge,
                            obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getChargeList().add(charge);
                }
            });

        }


        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            this.getPreList().add(map.get(iterator.next()));
        }

    }

    @Data
    public class PrescriptVo {

        public PrescriptVo(String type,PreVo.ItemVo item,PreVo.InspectVo inspect,PreVo.ChargeVo charge,String requirement,String remark){
            this.type = type;
            if(null != item)this.itemList.add(item);
            if(null != inspect)this.inspectList.add(inspect);
            if(null != charge)this.chargeList.add(charge);
            this.requirement = requirement;
            this.remark = remark;
        }

        @ApiModelProperty("1:药品处方；2：检查处方")
        private String type;

        @ApiModelProperty("药品集合")
        private List<PreVo.ItemVo> itemList = new ArrayList<>();

        @ApiModelProperty("药品集合")
        private List<PreVo.InspectVo> inspectList = new ArrayList<>();

        @ApiModelProperty("附加费")
        private List<PreVo.ChargeVo> chargeList = new ArrayList<>();

        @ApiModelProperty("要求")
        private String requirement;

        @ApiModelProperty("备注")
        private String remark;

    }



    @Data
    public class InspectVo{

        @ApiModelProperty("检查目的")
        private String aim;

        @ApiModelProperty("检查部位")
        private String part;

        @ApiModelProperty("检查类型")
        private String category;

        @ApiModelProperty("检查类型名称")
        private String categoryName;

        @ApiModelProperty("病情摘要")
        private String summary;

        @ApiModelProperty("检查诊断")
        private String diagnosis;

        @ApiModelProperty("费用")
        private Double fee;

        @ApiModelProperty("备注")
        private String memo;

        @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
        private Integer payStatus;

    }

    @Data
    public class ChargeVo{
        @ApiModelProperty("费用类型")
        private String category;

        @ApiModelProperty("检查类型名称")
        private String categoryName;

        @ApiModelProperty("费用")
        private Double fee;

        @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
        private Integer payStatus;
    }

    @Data
    public class ItemVo{
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("生产厂家名称")
        private String manufacturerName;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("频率")
        private Integer frequency;

        @ApiModelProperty("频率")
        private String frequencyName;

        @ApiModelProperty("单次剂量")
        private Double singleDose;

        @ApiModelProperty("剂量单位名称")
        private String doseUnitName;

        @ApiModelProperty("一级单位名称")
        private String unitName;

        @ApiModelProperty("小单位名称")
        private String minUnitName;

        @ApiModelProperty("二级单位名称 药品 minPriceUnitType：1代表minUnit,2代表doseUnit")
        private String minOrDoseUnitName;

        @ApiModelProperty("二级零售价对应单位  1：小单位minUnit，2：剂量单位doseUnit")
        private Integer minPriceUnitType;

        @ApiModelProperty("一单位零售价")
        private Double retailPrice;

        @ApiModelProperty("二级单位零售价")
        private Double minRetailPrice;

        @ApiModelProperty("单位类型 1：一级计价单位，2：二级计价单位")
        private Integer unitType;

        @ApiModelProperty("备注")
        private String memo;

        @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
        private Integer payStatus;

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("总价")
        private Double totalFee;
    }

    @Data
    public class PatientVo{
        @ApiModelProperty("用户主键 非必填 null:系统自动添加用户；not null：系统编辑用户")
        private String patientId;

        @ApiModelProperty("用户姓名")
        private String userName;

        @ApiModelProperty("性别 0:男;1:女")
        private String gender;

        @ApiModelProperty("出生日期")
        private String dateOfBirth;

        @ApiModelProperty("号码")
        private String mobile;

        @ApiModelProperty("地址")
        private String address;

        @ApiModelProperty("过敏史")
        private String anaphylaxis;

        @ApiModelProperty("身份证")
        private String idCard;

        @ApiModelProperty("社保卡")
        private String socialCard;

        @ApiModelProperty
        private Integer age;

    }


    @Data
    public class MedicalRecordVo{
        @ApiModelProperty("主诉")
        private String chiefComplaint;

        @ApiModelProperty("诊断信息")
        private String diagnosis;

        @ApiModelProperty("医嘱")
        private String advice;

        @ApiModelProperty("1 初诊、0 复诊")
        private String isFirst;

        @ApiModelProperty("发病日期")
        private String onSetDate;

        @ApiModelProperty("是否传染病 0：否；1：是")
        private String isContagious;

        @ApiModelProperty("备注")
        private String remark;

    }
}
