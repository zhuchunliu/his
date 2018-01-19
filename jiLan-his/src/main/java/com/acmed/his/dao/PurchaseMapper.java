package com.acmed.his.dao;

import com.acmed.his.model.Purchase;
import com.acmed.his.model.dto.PurchaseDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-03
 **/
public interface PurchaseMapper extends TkMapper<Purchase> {
    List<PurchaseDto> getAuditList(@Param("orgCode") Integer orgCode, @Param("purchaseNo") String purchaseNo,
                                   @Param("status") Integer status, @Param("supplierId") Integer supplierId,
                                   @Param("startTime") String startTime, @Param("endTime") String endTime);

    Double getCurrentDayFee(@Param("orgCode") Integer orgCode);

    Double getSurveyFee(@Param("orgCode") Integer orgCode,
                        @Param("startTime") String startTime,
                        @Param("endTime") String endTime);
}
