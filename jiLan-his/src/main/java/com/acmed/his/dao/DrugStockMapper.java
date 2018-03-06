package com.acmed.his.dao;

import com.acmed.his.model.DrugStock;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-16
 **/
public interface DrugStockMapper extends TkMapper<DrugStock>{
    List<DrugStock> getByDrugId(@Param("drugId") Integer drugId);
}
