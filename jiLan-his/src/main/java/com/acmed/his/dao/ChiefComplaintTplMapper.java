package com.acmed.his.dao;

import com.acmed.his.model.ChiefComplaintTpl;
import com.acmed.his.model.dto.ChiefComplaintTplDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Darren on 2018-06-12
 **/
public interface ChiefComplaintTplMapper extends TkMapper<ChiefComplaintTpl>{

    List<Map<String,Object>> getChiefComplaintTplList(@Param("userInfo") UserInfo userInfo);

    List<ChiefComplaintTplDto> getChiefComplaintTplPageList(@Param("name") String name , @Param("isPublic") Integer isPublic,
                                                           @Param("isValid") String isValid, @Param("userInfo") UserInfo userInfo,
                                                           @Param("dicTypeCode")String dicTypeCode);

    Integer getChiefComplaintTplTotal(@Param("name") String name ,@Param("isPublic") Integer isPublic,
                              @Param("isValid") String isValid,@Param("userInfo") UserInfo userInfo,
                              @Param("dicTypeCode")String dicTypeCode);
}
