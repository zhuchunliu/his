package com.acmed.his.dao;

import com.acmed.his.model.Drug;
import com.acmed.his.model.dto.DrugDto;
import com.acmed.his.model.dto.DrugStockDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface DrugMapper extends TkMapper<Drug>{
    List<DrugDto> getDrugList(@Param("orgCode") Integer orgCode, @Param("name") String name,
                              @Param("category") String category, @Param("isValid") String isValid);

    Integer getDrugTotal(@Param("orgCode") Integer orgCode, @Param("name") String name,
                         @Param("category") String category,  @Param("isValid") String isValid);

    List<DrugStockDto> getStockList(@Param("name") String name, @Param("orgCode") Integer orgCode);

    Integer getStockTotal(@Param("name") String name, @Param("orgCode") Integer orgCode);

    Drug getByDrugCode(@Param("drugCode") String drugCode);
}
