package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HisApplicationForm
 * his产品使用申请留言板
 * @author jimson
 * @date 2018/6/4
 */
@Data
@Table(name = "tb_his_application_form")
@NameStyle
public class HisApplicationForm {
    @Id
    @ApiModelProperty("主键")
    private String id;

    @ApiModelProperty("联系方式")
    private String mobile;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("联系人")
    private String linkman;

    @ApiModelProperty("医院")
    private String hospital;

    @ApiModelProperty("状态  1待处理  2联系成功  3无效信息")
    private Integer status;

    @ApiModelProperty("创建时间")
    private String createAt;

    @ApiModelProperty("修改时间")
    private String modifyAt;

    @ApiModelProperty("创建人")
    private String createBy;

    @ApiModelProperty("修改人")
    private String modifyBy;
}
