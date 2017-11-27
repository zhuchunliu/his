package com.acmed.his.pojo.vo;

import com.acmed.his.model.Prescription;
import com.acmed.his.model.PrescriptionItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 药品处方
 * Created by Darren on 2017-11-22
 **/
@Data
public class PreMedicineVo {

    @ApiModelProperty
    private Integer id;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("药品集合")
    private List<Item> itemList;

    @ApiModelProperty("附加费")
    private List<Charge> chargeList;

    public PreMedicineVo(Prescription prescription, List<PrescriptionItem> preItemList, List<com.acmed.his.model.Charge> preChargeList) {
        this.id = prescription.getId();
        this.applyId = prescription.getApplyId();
        if(null != preItemList && 0 != preItemList.size()){
            itemList = new ArrayList<>();
            preItemList.forEach(obj->{
                Item item = new Item();
                BeanUtils.copyProperties(obj,item);
                itemList.add(item);
            });
        }

        if(null != preChargeList && 0 != preChargeList.size()){
            chargeList = new ArrayList<>();
            preChargeList.forEach(obj->{
                Charge charge = new Charge();
                BeanUtils.copyProperties(obj,charge);
                chargeList.add(charge);
            });
        }
    }


    @Data
    public class Item{
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("频率")
        private Integer frequency;

        @ApiModelProperty("疗程")
        private Integer course;
    }

    @Data
    public class Charge{
        @ApiModelProperty("费用类型")
        private String category;

//        @ApiModelProperty("费用")
//        private Double fee;
    }

}
