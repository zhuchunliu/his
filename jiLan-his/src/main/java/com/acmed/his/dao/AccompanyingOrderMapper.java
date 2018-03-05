package com.acmed.his.dao;

import com.acmed.his.model.AccompanyingOrder;
import com.acmed.his.model.dto.AccompanyingOrderCountDto;
import com.acmed.his.model.dto.AccompanyingOrderMo;
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
     * @return
     */
    List<AccompanyingOrder> selectByAccompanyingOrder(AccompanyingOrderMo accompanyingOrderMo);

    List<AccompanyingOrderCountDto> selectCountNumGroupByOrgCode(@Param("status") Integer status);
}
