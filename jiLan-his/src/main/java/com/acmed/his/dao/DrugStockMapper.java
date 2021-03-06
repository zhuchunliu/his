package com.acmed.his.dao;

import com.acmed.his.model.DrugStock;
import com.acmed.his.model.dto.DrugWarnDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-16
 **/
public interface DrugStockMapper extends TkMapper<DrugStock>{
    List<DrugStock> getByDrugId(@Param("drugId") Integer drugId);

    Integer isNeedWarn(@Param("orgCode") Integer orgCode,@Param("expiryDate") String expiryDate);

    List<DrugWarnDto> getWarnList(@Param("name") String name, @Param("orgCode") Integer orgCode,
                                  @Param("expiryDate") String expiryDate);

    Integer getWarnTotal(@Param("name") String name, @Param("orgCode") Integer orgCode,
                                  @Param("expiryDate") String expiryDate);

    List<DrugStock> getWarnDrug(@Param("drugId") Integer drugId, @Param("type") Integer type,
                                @Param("expiryDate") String expiryDate);
}
