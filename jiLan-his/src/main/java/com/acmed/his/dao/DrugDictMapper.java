package com.acmed.his.dao;

import com.acmed.his.model.DrugDict;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface DrugDictMapper extends TkMapper<DrugDict> {
    List<DrugDict> getDrugDictList(@Param("orgCode") Integer orgCode, @Param("name") String name, @Param("category") String category);

    Integer getDrugDictTotal(@Param("orgCode") Integer orgCode, @Param("name") String name, @Param("category") String category);
}
