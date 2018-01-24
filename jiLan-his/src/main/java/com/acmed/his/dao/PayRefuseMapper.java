package com.acmed.his.dao;

import com.acmed.his.model.PayRefuse;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * PayRefuseMapper
 *
 * @author jimson
 * @date 2017/11/23
 */
public interface PayRefuseMapper extends TkMapper<PayRefuse> {
    /**
     * 根据付费类型，统计指定日期流水
     * @param date
     * @param orgCode
     * @return
     */
    List<Map<String,Object>> getFeeSurvey(@Param("date") String date, @Param("orgCode") Integer orgCode);
}
