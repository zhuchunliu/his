package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionItemMapper extends TkMapper<PrescriptionItem> {
    void delByPreId(@Param("prescriptionId") String prescriptionId);
}