package com.acmed.his.dao;

import com.acmed.his.model.Apply;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface ApplyMapper extends TkMapper<Apply>{

    //TODO 统计挂号单总费用 ，暂时只统计了处方费用
    /**
     * 统计挂号单总费用 ，暂时只统计了处方费用
     * @param id
     */
    void statisFee(@Param("id") String id);

    /**
     * 获取发挂号单列表【发药收费用】
     * @param orgCode 机构编码
     * @param name 患者姓名,门诊编号
     * @param date 有效期
     * @param status 发药状态：0:未发药;1:已发药
     * @return
     */
    List<Apply> getApply(@Param("orgCode") Integer orgCode, @Param("name") String name, @Param("date") String date, @Param("status") String status);

    /**
     * 姓名模糊查询
     * @param dept 科室id
     * @param date 日期
     * @param status 状态
     * @param name 姓名
     * @param isPaid 姓名
     * @return
     */
    List<Apply> mohu(@Param("dept") Integer dept,@Param("date") Date date,@Param("status") String status,@Param("name") String name,@Param("isPaid")String isPaid);

    /**
     * 获取机构的就诊量
     * @param orgCode
     */
    Integer getApplyNum(@Param("orgCode") Integer orgCode);
}
