package com.acmed.his.dao;

import com.acmed.his.model.AdviceTpl;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface AdviceTplMapper extends TkMapper<AdviceTpl> {
    List<AdviceTpl> getAdviceTplList(@Param("orgCode") Integer orgCode,@Param("isValid")String isValid);
}
