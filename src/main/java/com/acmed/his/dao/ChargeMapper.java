package com.acmed.his.dao;

import com.acmed.his.model.Charge;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2017-11-20
 **/
public interface ChargeMapper extends TkMapper<Charge>{
    void delByPreId(@Param("prescriptionId") Integer prescriptionId);

    void insertCharge(@Param("charge") Charge charge, @Param("prescriptionNo") String prescriptionNo);
}
