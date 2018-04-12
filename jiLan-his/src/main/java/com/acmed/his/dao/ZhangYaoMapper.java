package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.zy.OrderItemDrugDto;
import com.acmed.his.model.zy.ZyOrder;
import com.acmed.his.pojo.zy.ZyOrderQueryMo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-04-11
 **/
public interface ZhangYaoMapper {
    /**
     * 未下单的处方单
     * @param orgCode
     */
    List<OrderItemDrugDto> getUnOrder(@Param("orgCode") Integer orgCode);

    /**
     * 根据处方详情主键获取处方信息
     * @param ids
     * @return
     */
    List<PrescriptionItem> getPreItemByIds(@Param("ids") String[] ids);

    /**
     * 更新处方详情下单状态
     *
     * @param ids
     */
    void updateItemOrderStatus(@Param("ids") String[] ids);

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
}
