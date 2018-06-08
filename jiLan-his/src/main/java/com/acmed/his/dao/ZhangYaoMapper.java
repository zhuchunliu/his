package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.ZyOrder;
import com.acmed.his.model.dto.*;
import com.acmed.his.pojo.zy.ZYDispenseMo;
import com.acmed.his.pojo.zy.ZYHistoryQueryMo;
import com.acmed.his.pojo.zy.ZYReceiveQueryMo;
import com.acmed.his.pojo.zy.ZYUnpaidQueryMo;
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
     * 统计每个药被使用情况
     * @param drugIdList
     * @param orgCode
     * @return
     */
    List<Map<String,Object>> statisDrugUsed(@Param("drugIdList") List<Integer> drugIdList, @Param("orgCode") Integer orgCode);


    /**
     * 待支付列表
     * @param orgCode
     * @param mo
     * @return
     */
    List<ZyOrderItemUnpaidDto> getUnpaidOrder(@Param("mo") ZYUnpaidQueryMo mo, @Param("orgCode") Integer orgCode);

    /**
     * 待支付详情列表
     * @param name
     * @return
     */
    List<ZyOrderItemUnpaidDetailDto> getUnpaidDetail(@Param("groupNum") String groupNum, @Param("name") String name);

    /**
     * 获取历史订单
     * @param orgCode
     * @param mo
     * @return
     */
    List<ZyOrderItemHistoryDto> getHistoryOrder(@Param("orgCode") Integer orgCode, @Param("mo") ZYHistoryQueryMo mo);

    /**
     * 获取收货列表
     * @param orgCode
     * @param mo
     * @return
     */
    List<ZyOrderItemReceiveDto> getReceiveOrder(@Param("orgCode") Integer orgCode, @Param("mo") ZYReceiveQueryMo mo);

    /**
     *
     * 获取患者发放列表
     * @param orgCode
     * @param zyDispenseMo
     * @return
     */
    List<ZyDispenseOrderDto> getDispenseOrder(@Param("orgCode") Integer orgCode, @Param("mo") ZYDispenseMo zyDispenseMo);


    /**
     * 获取收货详情
     *
     * @param applyId
     * @return
     */
    List<ZyDispenseOrderDetailDto> getDispenseDetail(@Param("applyId") String applyId);

    /**
     * 更新处方详情为拆单状态
     *
     * @param ids
     */
    void updatePreItemStatusByIds(@Param("ids") List<String> ids,@Param("zyOrderStatus") Integer zyOrderStatus);


    /**
     * 根据掌药订单id设置处方单状态
     *
     * @param orderId
     * @param zyOrderStatus
     */
    void updatePreItemStatusByOrderId(@Param("orderId") String orderId, @Param("zyOrderStatus") Integer zyOrderStatus);

    /**
     * 根据处方id设置处方单状态
     *
     * @param id
     * @param zyOrderStatus
     */
    void updatePreItemStatusById(@Param("id") String id, @Param("zyOrderStatus") Integer zyOrderStatus);



}
