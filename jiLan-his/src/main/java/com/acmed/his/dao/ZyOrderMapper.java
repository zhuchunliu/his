package com.acmed.his.dao;

import com.acmed.his.model.ZyOrder;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    /**
     * 根据掌药id集合获取订单信息
     * @param zyOrderIds
     * @return
     */
    void updateZyOrderItem(@Param("zyOrderIds") String[] zyOrderIds);

    /**
     * 设置反馈订单为支付成功
     * @param orderIds
     */
    void updateZyOrderFeedBack(@Param("zyOrderIds") String[] orderIds,@Param("orderstate")String orderstate);

    /**
     * 设置订单关联的掌药订单
     * @param orderIds
     */
    void updateZyOrderId(@Param("zyOrderIds") String[] orderIds);

    /**
     * 获取需要推送的用户主键
     * @param orderIds
     * @return
     */
    List<Map<String,Object>> getPushUserId(@Param("zyOrderIds") String[] orderIds);
}
