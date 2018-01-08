package com.acmed.his.dao;

import com.acmed.his.model.DrugDay;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2018-01-05
 **/
public interface DrugDayMapper extends TkMapper<DrugDay>{
    void staticDrug(@Param("orgCode") Integer orgCode);
}
