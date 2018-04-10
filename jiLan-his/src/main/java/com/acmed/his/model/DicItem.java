package com.acmed.his.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import tk.mybatis.mapper.annotation.NameStyle;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * DicItem
 * 字典项
 * @author jimson
 * @date 2017/11/20
 */
@Data
@Table(name = "t_p_s_dicitem")
@NameStyle
public class DicItem implements Serializable{

    private static final long serialVersionUID = -3373944916158625893L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ApiModelProperty(value = "主键不做业务",hidden = true)
    private Integer id;

    @ApiModelProperty("字典项编码")
    private String dicItemCode;

    @ApiModelProperty("字典项名称")
    private String dicItemName;

    @ApiModelProperty("字典类型编码")
    private String dicTypeCode;

    @ApiModelProperty("所属机构")
    private Integer orgCode;

    @ApiModelProperty("删除标识:0 未删除,1:删除")
    private String removed;

    @ApiModelProperty("开始时间")
    private String startTime;

    @ApiModelProperty("结束时间")
    private String endTime;
}
