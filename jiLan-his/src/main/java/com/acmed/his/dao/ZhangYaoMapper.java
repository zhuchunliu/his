package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.model.dto.ZyOrderItemUnpaidDto;
import com.acmed.his.model.dto.ZyOrderItemUnsubmitDto;
import com.acmed.his.pojo.zy.ZyOrderQueryMo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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
     * 根据药店，获取待提交的订单
     * @param orgCode
     * @param storeIdList
     * @return
     */
    List<ZyOrder> getUnSubmitOrder(@Param("orgCode") Integer orgCode, @Param("storeIdList") List<String> storeIdList);


    /**
     * 获取待提交的订单详情
     * @param orgCode
     * @return
     */
    List<ZyOrderItemUnsubmitDto> getUnSubmitOrderItem(@Param("orgCode") Integer orgCode, @Param("name")String name);

    /**
     * 更新处方详情为拆单状态
     *
     * @param ids
     */
    void updateItemDismantleStatus(@Param("ids") List<String> ids);


    /**
     * 统计每个药被使用情况
     * @param drugIdList
     * @param orgCode
     * @return
     */
    List<Map<String,Object>> statisDrugUsed(@Param("drugIdList") List<Integer> drugIdList, @Param("orgCode") Integer orgCode);

    /**
     * 待支付详情列表
     * @param orgCode
     * @param name
     * @return
     */
    List<ZyOrderItemUnpaidDto> getUnpaidOrder(Integer orgCode, String name);
}
