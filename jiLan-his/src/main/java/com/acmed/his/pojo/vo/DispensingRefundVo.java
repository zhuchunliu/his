package com.acmed.his.pojo.vo;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.DrugMapper;
import com.acmed.his.model.*;
import com.acmed.his.service.BaseInfoManager;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.Id;
import java.util.*;

/**
 * 发药处方列表
 *
 * Created by Darren on 2017-12-05
 **/
@Data
public class DispensingRefundVo {

    @ApiModelProperty("处方编号")
    private String prescriptionNo;

    @ApiModelProperty("处方总金额")
    private Double totalFee;


    @ApiModelProperty("处方集合")
    private List<DispensingRefundVo.DisPrescriptVo> preList = new ArrayList<>();

    public DispensingRefundVo(Prescription prescription,
                              List<PrescriptionItem> itemList, List<Inspect> inspectList, List<Charge> chargeList,
                              BaseInfoManager baseInfoManager, DrugMapper drugMapper){

        this.prescriptionNo = prescription.getPrescriptionNo();
        this.totalFee = prescription.getFee();

        List<DicItem> unitItemList = baseInfoManager.getDicItemsByDicTypeCode(DicTypeEnum.UNIT.getCode());
        Map<String,String> unitItemName = Maps.newHashMap();
        unitItemList.forEach(obj->{
            unitItemName.put(obj.getDicItemCode(),obj.getDicItemName());
        });

        Map<String,DisPrescriptVo> map = new TreeMap<>();

        if(null != itemList && 0 != itemList.size()){
            itemList.forEach(obj->{
                DispensingRefundVo.DisItemVo item = new DispensingRefundVo.DisItemVo();
                BeanUtils.copyProperties(obj,item);
                if(StringUtils.isNotEmpty(obj.getZyStoreId())){
                    item.setNumName(obj.getNum()+Optional.ofNullable(obj.getZyDrugUnitName()).orElse(""));
                    item.setIsZyDrug(1);
                }else {
                    item.setIsZyDrug(0);
                    Drug drug = drugMapper.selectByPrimaryKey(obj.getDrugId());
                    if (1 == obj.getUnitType()) {
                        item.setUnit(null == drug.getUnit() ? "" : unitItemName.get(drug.getUnit().toString()));
                    } else {
                        if (1 == obj.getMinPriceUnitType()) {
                            item.setUnit(null == drug.getMinUnit() ? "" : unitItemName.get(drug.getMinUnit().toString()));
                        } else {
                            item.setUnit(null == drug.getDoseUnit() ? "" : unitItemName.get(drug.getDoseUnit().toString()));
                        }
                    }

                    if (1 == obj.getUnitType()) {
                        item.setNumName(obj.getNum() + (null == drug.getUnit() ? "" : unitItemName.get(drug.getUnit().toString())));
                    } else if (1 == obj.getMinPriceUnitType()) {
                        item.setNumName(obj.getNum() + (null == drug.getMinUnit() ? "" : unitItemName.get(drug.getMinUnit().toString())));
                    } else {
                        item.setNumName(obj.getNum() + (null == drug.getDoseUnit() ? "" : unitItemName.get(drug.getDoseUnit().toString())));
                    }
                }


                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new DisPrescriptVo(item,null,null
                            ,obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getItemList().add(item);
                }
            });

        }

        if(null != inspectList && 0 != inspectList.size()){
            inspectList.forEach((obj)->{
                DispensingRefundVo.DisInspectVo inspect = new DispensingRefundVo.DisInspectVo();
                BeanUtils.copyProperties(obj,inspect);
                inspect.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.INSPECT_CATEGORY.getCode(),obj.getCategory()).getDicItemName());
                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new DisPrescriptVo(null,inspect,null
                            ,obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getInspectList().add(inspect);
                }
            });

        }

        if(null != chargeList && 0 != chargeList.size()){
            chargeList.forEach((obj)->{
                DispensingRefundVo.DisChargeVo charge = new DispensingRefundVo.DisChargeVo();
                BeanUtils.copyProperties(obj,charge);
                charge.setCategoryName(baseInfoManager.getDicItem(DicTypeEnum.CHARGE_CATEGORY.getCode(),obj.getCategory()).getDicItemName());
                if(!map.containsKey(obj.getGroupNum())){
                    map.put(obj.getGroupNum(),new DisPrescriptVo(null,null,charge,
                            obj.getRequirement(),obj.getRemark()));
                }else{
                    map.get(obj.getGroupNum()).getChargeList().add(charge);
                }
            });

        }

        List<DisPrescriptVo> list = Lists.newArrayList();

        Iterator iterator = map.keySet().iterator();
        while (iterator.hasNext()){
            list.add(map.get(iterator.next()));
        }
        this.preList = list;

    }

    @Data
    public class DisPrescriptVo{

        private List<DisItemVo> itemList =  new ArrayList<>();
        private List<DisInspectVo> inspectList =  new ArrayList<>();
        private List<DisChargeVo> chargeList = new ArrayList<>();

        public DisPrescriptVo(DisItemVo itemVo, DisInspectVo inspectVo, DisChargeVo chargeVo, String requirement, String remark) {
            if(null != itemVo)this.itemList.add(itemVo);
            if(null != inspectVo)this.inspectList.add(inspectVo);
            if(null != chargeVo)this.chargeList.add(chargeVo);
        }
    }


    @Data
    public class DisInspectVo{

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
    public class DisChargeVo{

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

    @Data
    public static class DisItemVo{

        @ApiModelProperty("用药id")
        private String id;

        @ApiModelProperty("类型")
        private Integer type = 1;

        @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
        private Integer payStatus;

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("药品规格")
        private String spec;

        @ApiModelProperty("数量")
        private String numName;

        @ApiModelProperty("计价单位")
        private String unit;

        @ApiModelProperty("零售价")
        private Double retailPrice;

        @ApiModelProperty("费用")
        private Double fee;

        @ApiModelProperty("是否是云药房药品 0:否，1:是")
        private Integer isZyDrug;
    }




}
