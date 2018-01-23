package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.Id;
import javax.persistence.Table;

/**
 * DicItem
 * 字典项
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_dicitem")
@NameStyle
public class DicItem {

    @Id
    @ApiModelProperty("字典项编码")
    private String dicItemCode;

    @ApiModelProperty("字典项名称")
    private String dicItemName;

    @Id
    @ApiModelProperty("字典类型编码")
    private String dicTypeCode;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;
}
