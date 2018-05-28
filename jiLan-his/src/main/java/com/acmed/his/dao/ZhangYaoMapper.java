package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.zy.OrderItemDrugDto;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.model.dto.ZyOrderItemDto;
import com.acmed.his.pojo.zy.ZyOrderQueryMo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-04-11
 **/
public interface ZhangYaoMapper {
    /**
     * 未拆分的处方单
     * @param orgCode
     */
    List<PrescriptionItem> getUnDismantleList(@Param("orgCode") Integer orgCode);


    /**
     * 更新处方详情拆单状态
     *
     * @param ids
     */
    void updateItemDismantleStatus(@Param("ids") String[] ids);

    /**
     * 下单列表
     * @param zyOrderMo
     * @param orgCode
     */
    List<ZyOrder> getOrderList(@Param("mo") ZyOrderQueryMo zyOrderMo, @Param("orgCode") Integer orgCode);


    /**
     * 获取下单的药品详情
     * @param id
     * @return
     */
    List<OrderItemDrugDto> getOrderItemList(@Param("orderId") String orderId);

    /**
     * 获取待支付订单详情
     * @param orgCode
     * @return
     */
    List<ZyOrderItemDto> getUnpaidOrderItem(Integer orgCode);

    /**
     * 根据药店，获取待支付的订单
     * @param orgCode
     * @param storeIdList
     * @return
     */
    List<ZyOrder> getUnpaidOrder(@Param("orgCode") Integer orgCode, @Param("storeIdList") List<String> storeIdList);
}
