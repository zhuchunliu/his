package com.acmed.his.dao;

import com.acmed.his.model.DiagnosisTpl;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface DiagnosisTplMapper extends TkMapper<DiagnosisTpl>{
    List<DiagnosisTpl> getDiagnosisTplList(@Param("orgCode") Integer orgCode,@Param("isValid") String isValid);
}
