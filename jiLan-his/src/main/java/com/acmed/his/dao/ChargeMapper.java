package com.acmed.his.dao;

import com.acmed.his.model.Charge;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2017-11-20
 **/
public interface ChargeMapper extends TkMapper<Charge>{
    void delByPreId(@Param("prescriptionId") String prescriptionId);

    void refund(@Param("applyId") String applyId, @Param("groupNums") String[] groupNums);
}
