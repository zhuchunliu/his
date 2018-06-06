package com.acmed.his.dao;

import com.acmed.his.model.ZyOrderItem;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-04-12
 **/
public interface ZyOrderItemMapper extends TkMapper<ZyOrderItem> {
    /**
     * 根据orderId删除订单详情
     * @param orderId
     */
    void deleteByOrderId(@Param("orderId") String orderId);

    /**
     * 根据id删除订单详情
     * @param id
     */
    void deleteById(@Param("id") String id);

    /**
     * 根据orderId查找详情信息，排除掉删除的数据
     * @param orderId
     * @return
     */
    List<ZyOrderItem> getItemByOrderIdExclueRemove(@Param("orderId") String orderId);

    /**
     * 过滤掉删除状态的明细，设置成正常状态0
     * @param orderId
     * @param itemIdList
     */
    void updateNormalStatus(@Param("orderId") String orderId, @Param("itemIdList") List<String> itemIdList);

    /**
     * 过滤删除状态的明细，设置成被删除状态2
     * @param orderId
     * @param itemIdList
     */
    void updateAddStatus(@Param("orderId") String orderId, @Param("itemIdList") List<String> itemIdList);

    /**
     * 过滤状态的明细，设置成被新增状态1
     * @param orderId
     * @param itemIdList
     */
    void updateRemoveStatus(@Param("orderId") String orderId, @Param("itemIdList") List<String> itemIdList);


}
