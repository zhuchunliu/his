package com.acmed.his.util;

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
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 每页显示记录数
     */
    private Long total;
    /**
     * 数组
     */
    private List<T> data;
}
