package com.acmed.his.dao;

import com.acmed.his.model.Apply;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2017-11-20
 **/
public interface ApplyMapper extends TkMapper<Apply>{

    //TODO 统计挂号单总费用 ，暂时只统计了处方费用
    /**
     * 统计挂号单总费用 ，暂时只统计了处方费用
     * @param id
     */
    void statisFee(@Param("id") Integer id);
}
