package com.acmed.his.dao;

import com.acmed.his.model.ZyOrderItem;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2018-04-12
 **/
public interface ZyOrderItemMapper extends TkMapper<ZyOrderItem> {
    void deleteByOrderId(@Param("orderId") String orderId);

    void deleteById(@Param("id") String id);
}
