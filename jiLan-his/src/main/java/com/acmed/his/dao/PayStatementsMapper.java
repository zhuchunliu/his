package com.acmed.his.dao;

import com.acmed.his.model.PayStatements;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * PayStatementsMapper
 *
 * @author jimson
 * @date 2017/11/23
 */
public interface PayStatementsMapper extends TkMapper<PayStatements> {
    /**
     * 根据付费类型，统计指定日期流水
     * @param orgCode
     * @return
     */
    List<Map<String,Object>> getFeeSurvey(@Param("startDate") String startDate,
                                          @Param("endDate") String endDate,
                                          @Param("orgCode") Integer orgCode);
}
