package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DicType
 * 字典类型
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_dictype")
@NameStyle
public class DicType {
    @Id
    @ApiModelProperty("字典类型编码")
    private String dicTypeCode;

    @ApiModelProperty("字典类型名称")
    private String dicTypeName;

    @Id
    @ApiModelProperty("产品编码")
    private String productCode;
}
