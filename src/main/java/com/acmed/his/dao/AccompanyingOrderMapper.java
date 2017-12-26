package com.acmed.his.dao;

import com.acmed.his.model.AccompanyingOrder;
import com.acmed.his.model.dto.AccompanyingOrderCountDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AccompanyingOrderMapper
 * 就医北上广订单
 * @author jimson
 * @date 2017/12/22
 */
public interface AccompanyingOrderMapper extends TkMapper<AccompanyingOrder> {

    /**
     * 条件
     * @param accompanyingOrder
     * @param orderBy 如 createat,status DESC
     * @return
     */
    List<AccompanyingOrder> selectByAccompanyingOrder(AccompanyingOrder accompanyingOrder,@Param("orderBy") String orderBy);

    List<AccompanyingOrderCountDto> selectCountNumGroupByOrgCode(@Param("status") Integer status);
}
