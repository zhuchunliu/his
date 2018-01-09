package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionItem;
import com.acmed.his.model.dto.DrugDayDetailDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionItemMapper extends TkMapper<PrescriptionItem> {
    void delByPreId(@Param("prescriptionId") String prescriptionId);

    List<DrugDayDetailDto> getItemList(@Param("orgCode") Integer orgCode,
                                          @Param("startTime") String startTime,
                                          @Param("endTime") String endTime);

    Integer getItemTotal(@Param("orgCode") Integer orgCode,
                            @Param("startTime") String startTime,
                            @Param("endTime") String endTime);
}
