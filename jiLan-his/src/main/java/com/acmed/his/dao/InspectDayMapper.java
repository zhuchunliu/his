package com.acmed.his.dao;

import com.acmed.his.model.InspectDay;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2018-01-05
 **/
public interface InspectDayMapper extends TkMapper<InspectDay>{
    void staticInspect(@Param("orgCode") Integer orgCode);
}