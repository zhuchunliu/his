package com.acmed.his.pojo.vo;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.model.*;
import com.acmed.his.service.BaseInfoManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-01-22
 **/
@Data
public class DispensingMedicineVo {

    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    @ApiModelProperty("费用合计")
    private Double totalFee;


    public DispensingMedicineVo(){

    }

    @ApiModelProperty("药品信息")
    private List<List<MedicalDetail>> detailList = Lists.newArrayList();

    public DispensingMedicineVo(Prescription prescription, List<PrescriptionItem> itemList,
                                Map<String, List<PrescriptionItemStock>> stockMap,
                                BaseInfoManager baseInfoManager, DrugMapper drugMapper) {
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
                    if(null != stock.getNum() && 0 != stock.getNum()){
                        medicalDetail.setNumName(stock.getNum()+unitItemName.get(drug.getUnit()));
                    }
                    if(null != stock.getMinNum() && 0 != stock.getMinNum()){
                        medicalDetail.setNumName(stock.getMinNum()+unitItemName.get(drug.getMinUnit()));
                    }
                    if(null != stock.getDoseNum() && 0 != stock.getDoseNum()){
                        medicalDetail.setNumName(stock.getDoseNum()+unitItemName.get(drug.getDoseUnit()));
                    }
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

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("零售价")
        private Double retailPrice;

        @ApiModelProperty("大单位数量")
        private String numName;

        @ApiModelProperty("频率")
        private Integer frequency;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("有效期")
        private String expiryDate;

        @ApiModelProperty("入库批号")
        private String batchNumber;

        @ApiModelProperty("要求")
        private String requirement;

        @ApiModelProperty("备注")
        private String remark;


    }


}
