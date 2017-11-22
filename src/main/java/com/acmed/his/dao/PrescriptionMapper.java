package com.acmed.his.dao;

import com.acmed.his.model.Prescription;
import com.acmed.his.model.dto.PreTitleDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionMapper extends TkMapper<Prescription>{
    Prescription getByNo(@Param("prescriptionNo") String prescriptionNo);

    List<PreTitleDto> getPreByApply(@Param("applyId") Integer applyId);
}
