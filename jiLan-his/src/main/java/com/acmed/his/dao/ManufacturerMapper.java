package com.acmed.his.dao;

import com.acmed.his.model.Manufacturer;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface ManufacturerMapper extends TkMapper<Manufacturer>{
    List<Manufacturer> getManufacturerLikeName(@Param("name") String name, @Param("orgCode") Integer orgCode);

    List<Manufacturer> getManufacturerEqualName(@Param("name") String name, @Param("orgCode") Integer orgCode);
}
