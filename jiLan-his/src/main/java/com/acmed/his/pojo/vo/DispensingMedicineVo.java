package com.acmed.his.pojo.vo;

import com.acmed.his.model.Prescription;
import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.PrescriptionItemStock;
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
                                Map<String, List<PrescriptionItemStock>> stockMap) {
        this.prescriptionNo = prescription.getPrescriptionNo();

        Map<String,List<PrescriptionItem>> itemMap = Maps.newHashMap();
        for(PrescriptionItem item : itemList){
            if(itemMap.containsKey(item.getGroupNum())){
                itemMap.get(item.getGroupNum()).add(item);
            }else{
                itemMap.put(item.getGroupNum(),Lists.newArrayList(item));
            }
        }


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

        @ApiModelProperty("数量")
        private Double num;

        @ApiModelProperty("频率")
        private Integer frequency;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("有效期")
        private String expiryDate;

        @ApiModelProperty("入库批号")
        private String batchNumber;


    }


}
