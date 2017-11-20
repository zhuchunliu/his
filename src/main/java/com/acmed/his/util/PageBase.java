package com.acmed.his.util;

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
    private Integer pageNum;
    /**
     * 每页显示记录数
     */
    private Integer pageSize;
    /**
     * 查询参数
     */
    private T param;
}
