package com.acmed.his.pojo.vo;

import com.acmed.his.model.Prescription;
import com.acmed.his.model.PrescriptionItem;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2017-11-30
 **/
@Data
public class PreVo {
    @ApiModelProperty
    private Integer id;

    @ApiModelProperty("挂号单号")
    private Integer applyId;

    @ApiModelProperty("药品集合")
    private List<PreVo.Inspect> inspectList;

    @ApiModelProperty("附加费")
    private List<PreVo.Charge> chargeList;

    @ApiModelProperty("药品集合")
    private List<PreVo.Item> itemList;

    public PreVo(Prescription prescription, List<com.acmed.his.model.Inspect> preInspectist,
                 List<com.acmed.his.model.Charge> preChargeList, List<com.acmed.his.model.PrescriptionItem> preItemList) {
        this.id = prescription.getId();
        this.applyId = prescription.getApplyId();
        if(null != preInspectist && 0 != preInspectist.size()){
            inspectList = new ArrayList<>();

            preInspectist.forEach((obj)->{
                PreVo.Inspect inspect = new PreVo.Inspect();
                BeanUtils.copyProperties(obj,inspect);
                inspectList.add(inspect);
            });
        }

        if(null != preChargeList && 0 != preChargeList.size()){
            chargeList = new ArrayList<>();
            preChargeList.forEach((obj)->{
                PreVo.Charge charge = new PreVo.Charge();
                BeanUtils.copyProperties(obj,charge);
                chargeList.add(charge);
            });
        }

        if(null != preItemList && 0 != preItemList.size()){
            itemList = new ArrayList<>();
            preItemList.forEach(obj->{
                PreVo.Item item = new PreVo.Item();
                BeanUtils.copyProperties(obj,item);
                item.setTotalFee(Optional.ofNullable(item.getNum()).orElse(0)*Optional.ofNullable(item.getFee()).orElse(0d));
                itemList.add(item);
            });
        }
    }



    @Data
    public class Inspect{

        @ApiModelProperty("检查目的")
        private String aim;

        @ApiModelProperty("检查部位")
        private String part;

        @ApiModelProperty("检查类型")
        private String category;

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
    public class Charge{
        @ApiModelProperty("费用类型")
        private String category;

        @ApiModelProperty("费用")
        private Double fee;
    }

    @Data
    public class Item{
        @ApiModelProperty("药品id")
        private Integer drugId;

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("途径")
        private String way;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("频率")
        private Integer frequency;

        @ApiModelProperty("单次剂量")
        private Integer dose;

        @ApiModelProperty("单价")
        private Double fee;

        @ApiModelProperty("备注")
        private String memo;

        @ApiModelProperty("药品编码")
        private String drugCode;

        @ApiModelProperty("总价")
        private Double totalFee;
    }
}
