package com.acmed.his.dao;

import com.acmed.his.model.DrugDict;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.TkMapper;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface DrugDictMapper extends TkMapper<DrugDict> {
    List<DrugDict> getOrgDrugDictList(@Param("orgCode") Integer orgCode, @Param("name") String name, @Param("category") String category);

    Integer getDrugDictTotal(@Param("orgCode") Integer orgCode, @Param("name") String name, @Param("category") String category);

    List<DrugDict> getDrugDictList(@Param("name") String name, @Param("category") String category,
                                   @Param("isHandle")Integer isHandle);
}
