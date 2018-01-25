package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2018-01-25
 **/
@Data
public class PrescriptionVo {

    @ApiModelProperty("1:药品处方；2：检查处方")
    private String type;

    @ApiModelProperty("药品集合")
    private List<PreVo.ItemVo> itemList = new ArrayList<>();

    @ApiModelProperty("药品集合")
    private List<PreVo.InspectVo> inspectList = new ArrayList<>();

    @ApiModelProperty("附加费")
    private List<PreVo.ChargeVo> chargeList = new ArrayList<>();

    public PrescriptionVo(String type,PreVo.ItemVo item,PreVo.InspectVo inspect,PreVo.ChargeVo charge){
        this.type = type;
        if(null != item)this.itemList.add(item);
        if(null != inspect)this.inspectList.add(inspect);
        if(null != charge)this.chargeList.add(charge);
    }

}
