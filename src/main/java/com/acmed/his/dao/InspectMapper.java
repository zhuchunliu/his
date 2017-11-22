package com.acmed.his.dao;

import com.acmed.his.model.Inspect;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2017-11-20
 **/
public interface InspectMapper extends TkMapper<Inspect> {
    void delByPreId(@Param("prescriptionId") Integer prescriptionId);
}
