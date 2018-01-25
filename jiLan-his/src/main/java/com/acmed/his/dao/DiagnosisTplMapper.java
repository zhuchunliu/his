package com.acmed.his.dao;

import com.acmed.his.model.DiagnosisTpl;
import com.acmed.his.model.dto.DiagnosisTplDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface DiagnosisTplMapper extends TkMapper<DiagnosisTpl>{
    List<DiagnosisTplDto> getDiagnosisTplList(@Param("name") String name , @Param("isPublic") Integer isPublic,
                                              @Param("isValid") String isValid, @Param("userInfo") UserInfo userInfo,
                                              @Param("dicTypeCode")String dicTypeCode);

    Integer getDiagnosisTplTotal(@Param("name") String name ,@Param("isPublic") Integer isPublic,
                                            @Param("isValid") String isValid,@Param("userInfo") UserInfo userInfo,
                                            @Param("dicTypeCode")String dicTypeCode);
}
