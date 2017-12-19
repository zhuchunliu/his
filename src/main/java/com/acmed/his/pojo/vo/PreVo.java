package com.acmed.his.pojo.vo;

import com.acmed.his.model.Prescription;
import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.pojo.mo.PreMo;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
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
    private List<Pre> PreList = new ArrayList<>();

    @Data
    public class Pre {

        public Pre(String type,PreVo.Item item,PreVo.Inspect inspect,PreVo.Charge charge){
            this.type = type;
            if(null != item)this.itemList.add(item);
            if(null != inspect)this.inspectList.add(inspect);
            if(null != charge)this.chargeList.add(charge);
        }

        @ApiModelProperty("1:药品处方；2：检查处方")
        private String type;

        @ApiModelProperty("药品集合")
        private List<PreVo.Item> itemList = new ArrayList<>();

        @ApiModelProperty("药品集合")
        private List<PreVo.Inspect> inspectList = new ArrayList<>();

        @ApiModelProperty("附加费")
        private List<PreVo.Charge> chargeList = new ArrayList<>();


    }

    public PreVo(Prescription prescription, List<com.acmed.his.model.Inspect> preInspectist,
                 List<com.acmed.his.model.Charge> preChargeList, List<com.acmed.his.model.PrescriptionItem> preItemList) {
        if(null == prescription){
            return;
        }
        this.id = prescription.getId();
        this.applyId = prescription.getApplyId();

        Map<String,Pre> map = new TreeMap<>();


        if(null != preItemList && 0 != preItemList.size()){
            preItemList.forEach(obj->{
                PreVo.Item item = new PreVo.Item();
                BeanUtils.copyProperties(obj,item);
                item.setTotalFee(Optional.ofNullable(item.getNum()).orElse(0)*Optional.ofNullable(item.getFee()).orElse(0d));

                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new Pre("1",item,null,null));
                }else{
                    map.get(obj.getGroupNum()).getItemList().add(item);
                    map.get(obj.getGroupNum()).setType("1");
                }
            });

        }

        if(null != preInspectist && 0 != preInspectist.size()){
            preInspectist.forEach((obj)->{
                PreVo.Inspect inspect = new PreVo.Inspect();
                BeanUtils.copyProperties(obj,inspect);

                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new Pre("2",null,inspect,null));
                }else{
                    map.get(obj.getGroupNum()).getInspectList().add(inspect);
                    map.get(obj.getGroupNum()).setType("2");
                }
            });

        }

        if(null != preChargeList && 0 != preChargeList.size()){
            preChargeList.forEach((obj)->{
                PreVo.Charge charge = new PreVo.Charge();
                BeanUtils.copyProperties(obj,charge);

                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new Pre(null,null,null,charge));
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
