package com.acmed.his.dao;

import com.acmed.his.model.Apply;
import com.acmed.his.model.dto.ApplyDoctorDto;
import com.acmed.his.model.dto.ChuZhenFuZhenCountDto;
import com.acmed.his.model.dto.DispensingDto;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

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
     *
     * @param orgCode 机构编码
     * @param name 患者姓名,门诊编号
     * @param status 发药状态：0:未发药;1:已发药
     * @return
     */
    List<DispensingDto> getDispensingList(@Param("orgCode") Integer orgCode, @Param("name") String name,  @Param("status") String status);

    /**
     * 模糊条件查询
     * @param orgCode 机构id
     * @param dept 科室id
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @param status 状态
     * @param param 条件
     * @param isPaid 是否支付
     * @return
     */
    List<ApplyDoctorDto> getByPinyinOrNameOrClinicnoTiaojian(@Param("orgCode") Integer orgCode,
                                                                   @Param("dept") Integer dept,
                                                                   @Param("startTime") String startTime,
                                                                   @Param("endTime") String endTime,
                                                                   @Param("status") String status,
                                                                   @Param("param") String param,
                                                                   @Param("isPaid")String isPaid);

    /**
     * 获取机构的就诊量
     * @param orgCode
     */
    Integer getApplyNum(@Param("orgCode") Integer orgCode);

    /**
     * 查询机构总的初诊和复诊数
     * @param orgCode 机构码
     * @return ChuZhenFuZhenCountDto
     */
    ChuZhenFuZhenCountDto chuZhenAndFuZhenTongJi(@Param("orgCode") Integer orgCode);
}
