package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionTpl;
import com.acmed.his.model.dto.PrescriptionTplDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionTplMapper extends TkMapper<PrescriptionTpl>{
    List<PrescriptionTplDto> getPrescripTplList(@Param("tplName") String tplName,@Param("category") String category,
                                                @Param("isPublic") String isPublic,@Param("isValid") String isValid,
                                                @Param("userInfo") UserInfo userInfo,@Param("dicTypeCode")String dicTypeCode);

    Integer getPrescripTplTotal(@Param("tplName") String tplName,@Param("category") String category,
                                @Param("isPublic") String isPublic,@Param("isValid") String isValid,
                                @Param("userInfo") UserInfo userInfo);

    PrescriptionTpl selectRecentTpl(PrescriptionTpl prescriptionTpl);

    List getGloablPrescripTplList(@Param("tplName") String tplName,@Param("category") String category);
}
