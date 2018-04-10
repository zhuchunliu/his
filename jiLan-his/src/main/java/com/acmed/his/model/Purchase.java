package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Darren on 2018-01-03
 **/
@Data
@Table(name = "t_b_purchase")
@NameStyle
public class Purchase {

    @Id
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("供应商")
    private Integer supplierId;

    @ApiModelProperty("采购编号")
    private String purchaseNo;

    @ApiModelProperty("采购员")
    private Integer userId;

    @ApiModelProperty("采购批发总额")
    private Double bidFee;

    @ApiModelProperty("采购处方总额")
    private Double retailFee;

    @ApiModelProperty("制单日期")
    private String date;

    @ApiModelProperty("审核标记；0未提交, 1：待审核, 2已审核 , 3:已驳回")
    private Integer status;

    @ApiModelProperty("审核人员")
    private Integer auditUserId;

    @ApiModelProperty("审核日期")
    private String auditDate;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;


}
