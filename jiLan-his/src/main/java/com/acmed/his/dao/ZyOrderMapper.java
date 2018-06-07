package com.acmed.his.dao;

import com.acmed.his.model.ZyOrder;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-04-12
 **/
public interface ZyOrderMapper extends TkMapper<ZyOrder> {
    /**
     * 根据掌药id集合获取订单信息
     * @param zyOrderIds
     * @return
     */
    void updateZyOrderPayStatus(@Param("zyOrderIds") String[] zyOrderIds,@Param("payStatus") Integer payStatus,
                                         @Param("payRemark")String payRemark);
}
