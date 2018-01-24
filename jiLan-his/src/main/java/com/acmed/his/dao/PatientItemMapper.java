package com.acmed.his.dao;

import com.acmed.his.model.PatientItem;
import com.acmed.his.model.dto.PatientItemDto;
import com.acmed.his.pojo.mo.PatientItemMo;
import com.acmed.his.util.TkMapper;

import java.util.List;

/**
 * PatientItemMapper
 * 机构患者信息表
 * @author jimson
 * @date 2018/1/17
 */
public interface PatientItemMapper extends TkMapper<PatientItem>{
    List<PatientItemDto> getByMohu(PatientItemMo patientItemMo);
}
