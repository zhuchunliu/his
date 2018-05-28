package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * 处方详情
 *
 * Created by Darren on 2017-11-20
 **/
@Data
@Table(name = "t_b_prescription_item")
@NameStyle
public class PrescriptionItem {

    @Id
    @ApiModelProperty("用药id")
    private String id;

    @ApiModelProperty("处方id")
    private String prescriptionId;

    @ApiModelProperty("药品id")
    private Integer drugId;

    @ApiModelProperty("挂号单号")
    private String applyId;

    @ApiModelProperty("药品类型")
    private String category;

    @ApiModelProperty("用药名称")
    private String drugName;

    @ApiModelProperty("药品编码")
    private String drugCode;

    @ApiModelProperty("途径")
    private String way;

    @ApiModelProperty("数量")
    private Integer num;

    @ApiModelProperty("单位类型 1：一级计价单位，2：二级计价单位")
    private Integer unitType;

    @ApiModelProperty("开处方时候：二级单位零售价对应单位  1：小单位minUnit，2：剂量单位doseUnit【防止后面修改药品二级单位】")
    private Integer minPriceUnitType;

    @ApiModelProperty("进价")
    private Double bid;

    @ApiModelProperty("零售价")
    private Double retailPrice;

    @ApiModelProperty("频率 字典表：DrugFrequency")
    private Integer frequency;

    @ApiModelProperty("单次剂量")
    private Double singleDose;

    @ApiModelProperty("费用")
    private Double fee;

    @ApiModelProperty("支付状态 0:未支付,1:已支付,2:已退款")
    private Integer payStatus;

    @ApiModelProperty("备注")
    private String memo;

    @ApiModelProperty("组号")
    private String groupNum;

    @ApiModelProperty("要求")
    private String requirement;

    @ApiModelProperty("服药备注")
    private String remark;

    @ApiModelProperty("药品id - 掌药")
    private Integer zyDrugId;

    @ApiModelProperty("药店id - 掌药")
    private String zyStoreId;

    @ApiModelProperty("药店名称 - 掌药")
    private String zyStoreName;

    @ApiModelProperty("药品规格 - 掌药")
    private String zyDrugSpec;

    @ApiModelProperty("药品厂家 - 掌药")
    private String zyManufacturerName;

    @ApiModelProperty("订单id - 掌药")
    private String zyOrderId;

    @ApiModelProperty("下单状态 null:不需要下单 0:未拆单，1:已经拆单,2:已经下单")
    private Integer zyOrderStatus;
}
