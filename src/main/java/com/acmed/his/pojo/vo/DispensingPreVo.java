package com.acmed.his.pojo.vo;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.model.Charge;
import com.acmed.his.model.Inspect;
import com.acmed.his.model.Prescription;
import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.service.BaseInfoManager;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 发药处方列表
 *
 * Created by Darren on 2017-12-05
 **/
@Data
public class DispensingPreVo {

    @ApiModelProperty("处方主键")
    private Integer id;

    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    @ApiModelProperty("总费用")
    private Double fee;


    private List<Charge> chargeList;
    private List<Item> itemList;

    public DispensingPreVo(Prescription prescription, List<com.acmed.his.model.Charge> chargeList, List<PrescriptionItem> preItemList, BaseInfoManager baseInfoManager) {
        this.prescriptionNo = prescription.getPrescriptionNo();
        this.fee = prescription.getFee();
        this.id = prescription.getId();

        this.itemList = new ArrayList<>();
        preItemList.forEach(obj->{
            Item item = new Item();
            BeanUtils.copyProperties(obj,item);
            this.itemList.add(item);
        });

        this.chargeList = new ArrayList<>();
        chargeList.forEach(obj->{
            Charge charge = new Charge();
            charge.setFee(obj.getFee());
            charge.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.CHARGE_CATEGORY.getCode(),obj.getCategory()).getDicItemName());
            this.chargeList.add(charge);
        });
    }


    @Data
    private class Charge{
        @ApiModelProperty("费用")
        private Double fee;

        @ApiModelProperty("费用类型")
        private String categoryName;
    }

    @Data
    private class Item{
        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("药品单价")
        private Double fee;

        @ApiModelProperty("药品数量")
        private Integer num;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("备注")
        private String memo;
    }




}
