package com.acmed.his.dao;

import com.acmed.his.model.Supply;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SupplyMapper
 *
 * @author jimson
 * @date 2017/11/21
 */
public interface SupplyMapper extends TkMapper<Supply> {
    List<Supply> selectMohu(@Param("param") String param);
}
