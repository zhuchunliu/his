package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionTpl;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionTplMapper extends TkMapper<PrescriptionTpl>{
    List<PrescriptionTpl> getPrescripTplList(@Param("") Integer orgCode);
}
