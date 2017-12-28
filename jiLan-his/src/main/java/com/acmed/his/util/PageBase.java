package com.acmed.his.util;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PageBase
 * 分页查询
 * @author jimson
 * @date 2017/11/20
 */
@Data
public class PageBase<T> {
    /**
     * 页数
     */
    @ApiModelProperty("当前页码,默认第一页")
    private Integer pageNum = 1;
    /**
     * 每页显示记录数
     */
    @ApiModelProperty("每页显示数，默认每页十条数据")
    private Integer pageSize = 10;
    /**
     * 查询参数
     */
    @ApiModelProperty("参数")
    private T param;
}
