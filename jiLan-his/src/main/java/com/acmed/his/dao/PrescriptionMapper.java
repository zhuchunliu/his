package com.acmed.his.dao;

import com.acmed.his.model.Prescription;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionMapper extends TkMapper<Prescription>{
    Prescription getByNo(@Param("prescriptionNo") String prescriptionNo);

    List<Prescription> getPreByApply(@Param("applyId") String applyId);

    /**
     * 获取发挂号单列表【发药收费用】
     *
     * @param orgCode 机构编码
     * @param name 患者姓名,门诊编号
     * @param status 发药状态：0:未发药;1:已发药
     * @return
     */
    List<DispensingDto> getDispensingList(@Param("orgCode") Integer orgCode, @Param("name") String name,
                                          @Param("status") String status, @Param("diagnoseStartDate") String diagnoseStartDate,
                                          @Param("diagnoseEndDate") String diagnoseEndDate);


    /**
     * 获取发挂号单列表【发药收费用】
     *
     * @param orgCode 机构编码
     * @param name 患者姓名,门诊编号
     * @param status 发药状态：0:未发药;1:已发药
     * @return
     */
    Integer getDispensingTotal(@Param("orgCode") Integer orgCode, @Param("name") String name,
                               @Param("status") String status,@Param("diagnoseStartDate") String diagnoseStartDate,
                               @Param("diagnoseEndDate") String diagnoseEndDate);

    /**
     * 统计机构费用
     * @param startDate
     * @param endDate
     * @param orgCode
     * @return
     */
    Double getFee(@Param("startDate") String startDate,
                  @Param("endDate") String endDate,
                  @Param("orgCode") Integer orgCode);
}
