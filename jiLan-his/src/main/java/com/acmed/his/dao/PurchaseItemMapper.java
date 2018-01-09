package com.acmed.his.dao;

import com.acmed.his.model.PurchaseItem;
import com.acmed.his.model.dto.PurchaseDayDetailDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-03
 **/
public interface PurchaseItemMapper extends TkMapper<PurchaseItem> {
    void delItemNotInIds(@Param("ids") List<String> ids, @Param("purchaseId") String purchaseId);

    List<PurchaseDayDetailDto> getItemList(@Param("orgCode") Integer orgCode,
                                           @Param("startTime") String startTime,
                                           @Param("endTime") String endTime);

    Integer getItemTotal(@Param("orgCode") Integer orgCode,
                      @Param("startTime") String startTime,
                      @Param("endTime") String endTime);
}
