package com.acmed.his.pojo.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 发药处方列表
 *
 * Created by Darren on 2017-12-05
 **/
@Data
public class DispensingPreVo {

    @ApiModelProperty("要求")
    private String requirement;

    @ApiModelProperty("备注")
    private String remark;


    private List<DisItemVo> itemList =  new ArrayList<>();
    private List<DisInspectVo> inspectList =  new ArrayList<>();
    private List<DisChargeVo> chargeList = new ArrayList<>();

    public DispensingPreVo(DisItemVo itemVo, DisInspectVo inspectVo, DisChargeVo chargeVo, String requirement, String remark) {

        this.requirement = requirement;
        this.remark = remark;
        if(null != itemVo)this.itemList.add(itemVo);
        if(null != inspectVo)this.inspectList.add(inspectVo);
        if(null != chargeVo)this.chargeList.add(chargeVo);

    }

    @Data
    public static class DisInspectVo{

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
    public static class DisChargeVo{
        @ApiModelProperty("费用")
        private Double fee;

        @ApiModelProperty("费用类型")
        private String categoryName;
    }

    @Data
    public static class DisItemVo{

        @ApiModelProperty("用药名称")
        private String drugName;

        @ApiModelProperty("药品规格")
        private String spec;

        @ApiModelProperty("数量")
        private Integer num;

        @ApiModelProperty("计价单位")
        private String unit;

        @ApiModelProperty("单价")
        private Double fee;

        @ApiModelProperty("总价")
        private Double totalFee;
    }




}
