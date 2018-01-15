package com.acmed.his.dao;

import com.acmed.his.model.Patient;
import com.acmed.his.model.dto.OrgPatientNumDto;
import com.acmed.his.model.dto.PatientCountDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PatientMapper extends TkMapper<Patient>{
    /**
     * 查询机构患者统计
     * @param orgCode 机构编号
     * @return List<PatientCountDto>
     */
    List<PatientCountDto> getPatientCount(@Param("orgCode") Integer orgCode);

    /**
     * 机构当日和总的患者数
     * @param orgCode 机构码
     * @param date 日期
     * @return OrgPatientNumDto
     */
    OrgPatientNumDto getDayNumAnTotalNum(@Param("orgCode") Integer orgCode,@Param("date") String date);

    /**
     * 机构患者库
     * @param orgCode 机构id
     * @return List<Patient>
     */
    List<Patient> getPatientPool(@Param("orgCode") Integer orgCode);

    /**
     * 机构黑名单
     * @param orgCode 机构id
     * @return List<Patient>
     */
    List<Patient> getPatientBlacklist(@Param("orgCode") Integer orgCode);
}
