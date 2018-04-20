package com.acmed.his.dao;

import com.acmed.his.model.InsuranceOrder;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * InsuranceOrderMapper
 *
 * @author jimson
 * @date 2018/4/19
 */
public interface InsuranceOrderMapper extends TkMapper<InsuranceOrder> {
    int insertInsuranceOrderList(@Param("createAt") String createAt,
                                 @Param("appointmentTime")String appointmentTime,
                                 @Param("startTime")String startTime,
                                 @Param("endTime")String endTime,
                                 @Param("price")Integer price);

    int updateId(@Param("appointmentTime")String appointmentTime);
}
