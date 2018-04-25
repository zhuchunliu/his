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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by Darren on 2018-01-22
 **/
@Data
public class DispensingMedicineVo {

    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    @ApiModelProperty("费用合计")
    private Double totalFee = 0d;


    public DispensingMedicineVo(){

    }

    @ApiModelProperty("药品信息")
    private List<List<MedicalDetail>> detailList = Lists.newArrayList();

    public DispensingMedicineVo(Prescription prescription, List<PrescriptionItem> itemList,
                                Map<String, List<PrescriptionItemStock>> stockMap,
                                BaseInfoManager baseInfoManager, DrugMapper drugMapper,
                                ManufacturerMapper manufacturerMapper) {
        this.prescriptionNo = prescription.getPrescriptionNo();

        Map<String,List<PrescriptionItem>> itemMap = Maps.newHashMap();
        for(PrescriptionItem item : itemList){
            if(itemMap.containsKey(item.getGroupNum())){
                itemMap.get(item.getGroupNum()).add(item);
            }else{
                itemMap.put(item.getGroupNum(),Lists.newArrayList(item));
            }
        }

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



        for(String groupNum : itemMap.keySet()){
            List<MedicalDetail> childList = Lists.newArrayList();
            for(PrescriptionItem item : itemMap.get(groupNum)){
                if(!stockMap.containsKey(item.getId())){
                    continue;
                }
                stockMap.get(item.getId()).forEach(stock->{
                    MedicalDetail medicalDetail = new MedicalDetail();
                    BeanUtils.copyProperties(stock,medicalDetail);
                    BeanUtils.copyProperties(item,medicalDetail);
                    Drug drug = drugMapper.selectByPrimaryKey(item.getDrugId());
                    medicalDetail.setSpec(drug.getSpec());
                    if(null != stock.getNum() && 0 != stock.getNum()){
                        medicalDetail.setPrice(drug.getRetailPrice()*stock.getNum());
                        medicalDetail.setNumName(Optional.ofNullable(medicalDetail.getNumName()).orElse("")+stock.getNum()+
                                (null == drug.getUnit()?"":unitItemName.get(drug.getUnit().toString())));
                    }
                    if(null != stock.getMinNum() && 0 != stock.getMinNum()){
                        medicalDetail.setPrice(drug.getMinRetailPrice()*stock.getMinNum());
                        medicalDetail.setNumName(Optional.ofNullable(medicalDetail.getNumName()).orElse("")+stock.getMinNum()+
                                (null == drug.getMinUnit()?"":unitItemName.get(drug.getMinUnit().toString())));
                    }
                    if(null != stock.getDoseNum() && 0 != stock.getDoseNum()){
                        medicalDetail.setPrice(drug.getMinRetailPrice()*stock.getDoseNum());
                        medicalDetail.setNumName(Optional.ofNullable(medicalDetail.getNumName()).orElse("")+
                                (0==stock.getDoseNum()*10%1? String.valueOf((int)Math.floor(stock.getDoseNum())):String.valueOf(stock.getDoseNum()))+
                                (null == drug.getDoseUnit()?"":unitItemName.get(drug.getDoseUnit().toString())));
                    }
                    medicalDetail.setManufacturerName(manufacturerMapper.selectByPrimaryKey(drug.getManufacturer()).getName());
                    medicalDetail.setFrequencyName(frequencyItemName.get(item.getFrequency().toString()));
                    medicalDetail.setDoseUnitName(null == drug.getDoseUnit()?"":unitItemName.get(drug.getDoseUnit().toString()));
                    this.totalFee += medicalDetail.getPrice();
                    childList.add(medicalDetail);
                });
            }
            this.detailList.add(childList);
        }

    }


    @Data
    public class MedicalDetail {

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("药品规格")
        private String spec;

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

        @ApiModelProperty("备注")
        private String memo;


    }
}
