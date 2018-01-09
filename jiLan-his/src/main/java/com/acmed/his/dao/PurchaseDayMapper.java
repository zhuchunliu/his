package com.acmed.his.dao;

import com.acmed.his.model.PurchaseDay;
import com.acmed.his.model.dto.PurchaseDayDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-09
 **/
public interface PurchaseDayMapper extends TkMapper<PurchaseDay> {

    void statisPurchase(@Param("orgCode") Integer orgCode,
                        @Param("startTime") String startTime,
                        @Param("endTime") String endTime);

    List<PurchaseDayDto> getPurchaseDayList(@Param("orgCode") Integer orgCode,
                                            @Param("startTime") String startTime,
                                            @Param("endTime") String endTime);

    Integer getPurchaseDayTotal(@Param("orgCode") Integer orgCode,
                                @Param("startTime") String startTime,
                                @Param("endTime") String endTime);
}
