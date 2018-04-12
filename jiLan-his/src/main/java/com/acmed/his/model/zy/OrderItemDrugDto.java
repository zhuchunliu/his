package com.acmed.his.model.zy;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 未给掌药下单的药品信息
 * Created by Darren on 2018-04-11
 **/
@Data
public class OrderItemDrugDto {

    @ApiModelProperty("处方药品详情id")
    private String id;

    @ApiModelProperty("药店id - 掌药")
    private String zyStoreId;

    @ApiModelProperty("药店名称 - 掌药")
    private String zyStoreName;

    @ApiModelProperty("药品类型 - 掌药")
    private String zyDrugSpec;

    @ApiModelProperty("药品厂家 - 掌药")
    private String zyManufacturerName;

    @ApiModelProperty("用药名称")
    private String drugName;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("挂号单号 医疗机构id+排序")
    private String clinicNo;

    @ApiModelProperty("患者姓名")
    private String patientName;

    @ApiModelProperty("就诊时间")
    private String diagnoseDate;

}
