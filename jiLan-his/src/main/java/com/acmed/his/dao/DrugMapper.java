package com.acmed.his.dao;

import com.acmed.his.model.Drug;
import com.acmed.his.model.dto.DrugStockDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface DrugMapper extends TkMapper<Drug>{
    List<Drug> getDrugList(@Param("orgCode") Integer orgCode, @Param("name") String name, @Param("category") String category);

    Integer getDrugTotal(@Param("orgCode") Integer orgCode, @Param("name") String name, @Param("category") String category);

    void saveDrugByDict(@Param("codes") String[] codes, @Param("info") UserInfo info);

    List<DrugStockDto> getStockList(@Param("name") String name, @Param("orgCode") Integer orgCode);
}
