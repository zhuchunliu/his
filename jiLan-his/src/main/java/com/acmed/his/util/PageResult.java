package com.acmed.his.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * PageResult
 * 分页查询结果
 * @author jimson
 * @date 2017/11/20
 */
@Data
public class PageResult<T> {
    /**
     * 页数
     */
    @ApiModelProperty("页码")
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    @ApiModelProperty("每页记录数")
    private Integer pageSize;
    /**
     * 每页显示记录数
     */
    @ApiModelProperty("总记录数")
    private Long total;
    /**
     * 数组
     */
    @ApiModelProperty("数据")
    private List<T> data;
}
