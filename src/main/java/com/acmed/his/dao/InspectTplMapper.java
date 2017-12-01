package com.acmed.his.dao;

import com.acmed.his.model.InspectTpl;
import com.acmed.his.model.PrescriptionTpl;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2017-12-01
 **/
public interface InspectTplMapper extends TkMapper<InspectTpl> {

    void deleteByTplId(@Param("tplId") Integer id);

    void insertItem(@Param("inspect") InspectTpl inspectTpl, @Param("tpl") PrescriptionTpl finalPrescriptionTpl);
}
