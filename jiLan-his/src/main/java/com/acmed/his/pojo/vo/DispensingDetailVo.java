package com.acmed.his.pojo.vo;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.dao.ManufacturerMapper;
import com.acmed.his.model.*;
import com.acmed.his.service.BaseInfoManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.*;

/**
 * Created by Darren on 2018-03-09
 **/
@Data
public class DispensingDetailVo {
    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    @ApiModelProperty("费用合计")
    private Double totalFee;


    private List<DispensingInfoVo> infoVoList;
    public DispensingDetailVo(){

    }

    @Data
    public static class DispensingInfoVo{

        @ApiModelProperty("药品信息")
        private List<DispensingDetailVo.MedicalInfoVo>  medicalInfoList= Lists.newArrayList();

        @ApiModelProperty("检查信息")
        private List<DispensingDetailVo.InspectInfoVo> inspectList =  new ArrayList<>();

        @ApiModelProperty("附加信息")
        private List<DispensingDetailVo.ChargeInfoVo> chargeList = new ArrayList<>();


        public DispensingInfoVo(DispensingDetailVo.MedicalInfoVo itemVo, DispensingDetailVo.InspectInfoVo inspectVo, DispensingDetailVo.ChargeInfoVo chargeVo, String requirement, String remark) {
            if(null != itemVo)this.medicalInfoList.add(itemVo);
            if(null != inspectVo)this.inspectList.add(inspectVo);
            if(null != chargeVo)this.chargeList.add(chargeVo);
        }

    }


    public DispensingDetailVo(Prescription prescription, List<PrescriptionItem> itemList,
                                List<Inspect> inspectList,List<Charge> chargeList,
                                Map<String, List<PrescriptionItemStock>> stockMap,
                                BaseInfoManager baseInfoManager, DrugMapper drugMapper,
                                ManufacturerMapper manufacturerMapper) {
        this.prescriptionNo = prescription.getPrescriptionNo();


        List<DicItem> unitItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> unitItemName = Maps.newHashMap();
        unitItemList.forEach(obj->{
            unitItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        List<DicItem> frequencyItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.DRUG_FREQUENCY.getCode());
        Map<String,String> frequencyItemName = Maps.newHashMap();
        frequencyItemList.forEach(obj->{
            frequencyItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });


        Map<String,DispensingInfoVo> map = new TreeMap<>();

        for(PrescriptionItem item : itemList){

            stockMap.get(item.getId()).forEach(stock->{
                MedicalInfoVo medicalDetail = new MedicalInfoVo();
                BeanUtils.copyProperties(stock,medicalDetail);
                BeanUtils.copyProperties(item,medicalDetail);
                Drug drug = drugMapper.selectByPrimaryKey(item.getDrugId());
                if(null != stock.getNum() && 0 != stock.getNum()){
                    medicalDetail.setPrice(drug.getRetailPrice());
                    medicalDetail.setNumName(Optional.ofNullable(medicalDetail.getNumName()).orElse("")+stock.getNum()+
                            (null == drug.getUnit()?"":unitItemName.get(drug.getUnit().toString())));
                }
                if(null != stock.getMinNum() && 0 != stock.getMinNum()){
                    medicalDetail.setPrice(drug.getMinRetailPrice());
                    medicalDetail.setNumName(Optional.ofNullable(medicalDetail.getNumName()).orElse("")+stock.getMinNum()+
                            (null == drug.getMinUnit()?"":unitItemName.get(drug.getMinUnit().toString())));
                }
                if(null != stock.getDoseNum() && 0 != stock.getDoseNum()){
                    medicalDetail.setPrice(drug.getMinRetailPrice());
                    medicalDetail.setNumName(Optional.ofNullable(medicalDetail.getNumName()).orElse("")+
                            (0==stock.getDoseNum()*10%1? String.valueOf((int)Math.floor(stock.getDoseNum())):String.valueOf(stock.getDoseNum()))+
                            (null == drug.getDoseUnit()?"":unitItemName.get(drug.getDoseUnit().toString())));
                }
                medicalDetail.setManufacturerName(manufacturerMapper.selectByPrimaryKey(drug.getManufacturer()).getName());
                medicalDetail.setFrequencyName(frequencyItemName.get(item.getFrequency().toString()));
                medicalDetail.setDoseUnitName(null == drug.getDoseUnit()?"":unitItemName.get(drug.getDoseUnit().toString()));

                if(!map.containsKey(item.getGroupNum())){
                    map.put(item.getGroupNum(),new DispensingInfoVo(medicalDetail,null,null
                            ,item.getRequirement(),item.getRemark()));
                }else{
                    map.get(item.getGroupNum()).getMedicalInfoList().add(medicalDetail);
                }
            });
        }


        if(null != inspectList && 0 != inspectList.size()){
            inspectList.forEach((obj)->{
                InspectInfoVo inspect = new InspectInfoVo();
                BeanUtils.copyProperties(obj,inspect);
                inspect.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.INSPECT_CATEGORY.getCode(),obj.getCategory()).getDicItemName());
                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new DispensingInfoVo(null,inspect,null
                            ,obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getInspectList().add(inspect);
                }
            });

        }

        if(null != chargeList && 0 != chargeList.size()){
            chargeList.forEach((obj)->{
                ChargeInfoVo charge = new ChargeInfoVo();
                BeanUtils.copyProperties(obj,charge);
                charge.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.CHARGE_CATEGORY.getCode(),obj.getCategory()).getDicItemName());
                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new DispensingInfoVo(null,null,charge,
                            obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getChargeList().add(charge);
                }
            });

        }
        List<DispensingInfoVo> list = Lists.newArrayList();

        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            list.add(map.get(iterator.next()));
        }
        this.infoVoList = list;
    }




    @Data
    public static class MedicalInfoVo {

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("零售价")
        private Double price;

        @ApiModelProperty("大单位数量")
        private String numName;

        @ApiModelProperty("生产厂家名称")
        private String manufacturerName;

        @ApiModelProperty("频率")
        private String frequencyName;

        @ApiModelProperty("单次剂量")
        private Double singleDose;

        @ApiModelProperty("剂量单位")
        private String doseUnitName;

        @ApiModelProperty("有效期")
        private String expiryDate;

        @ApiModelProperty("入库批号")
        private String batchNumber;

        @ApiModelProperty("要求")
        private String requirement;

        @ApiModelProperty("备注")
        private String remark;


    }


    @Data
    public static  class InspectInfoVo{

        @ApiModelProperty("检查id")
        private String id;

        @ApiModelProperty("类型")
        private Integer type = 2;

        @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
        private Integer payStatus;

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

    }


    @Data
    public static class ChargeInfoVo{

        @ApiModelProperty("附加费用id")
        private String id;

        @ApiModelProperty("类型")
        private Integer type = 3;

        @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
        private Integer payStatus;

        @ApiModelProperty("费用")
        private Double fee;

        @ApiModelProperty("费用类型")
        private String categoryName;
    }
}
