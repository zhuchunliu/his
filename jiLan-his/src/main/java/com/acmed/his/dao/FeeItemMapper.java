package com.acmed.his.dao;

import com.acmed.his.model.FeeItem;
import com.acmed.his.model.dto.FeeItemDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface FeeItemMapper extends TkMapper<FeeItem>{

    List<FeeItemDto> getFeeItemList(@Param("orgCode") Integer orgCode, @Param("isValid") String isValid,
                                    @Param("feeCategory") String feeCategory, @Param("category") String category,
                                    @Param("categoryName") String categoryName);
}
