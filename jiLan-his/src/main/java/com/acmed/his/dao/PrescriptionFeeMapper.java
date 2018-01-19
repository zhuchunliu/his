package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionFee;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2018-01-18
 **/
public interface PrescriptionFeeMapper extends TkMapper<PrescriptionFee> {
    void delByPreId(@Param("prescriptionId") String prescriptionId);

}
