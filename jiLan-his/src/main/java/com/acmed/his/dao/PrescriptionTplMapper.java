package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionTpl;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionTplMapper extends TkMapper<PrescriptionTpl>{
    List<PrescriptionTpl> getPrescripTplList(@Param("orgCode") Integer orgCode,@Param("isValid")String isValid,@Param("category")String category);

    PrescriptionTpl selectRecentTpl(PrescriptionTpl prescriptionTpl);
}
