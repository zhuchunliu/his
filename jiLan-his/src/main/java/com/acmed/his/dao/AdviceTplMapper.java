package com.acmed.his.dao;

import com.acmed.his.model.AdviceTpl;
import com.acmed.his.model.dto.AdviceTplDto;
import com.acmed.his.model.dto.DiagnosisTplDto;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface AdviceTplMapper extends TkMapper<AdviceTpl> {

    List<AdviceTplDto> getAdviceTplList(@Param("name") String name , @Param("isPublic") Integer isPublic,
                                        @Param("isValid") String isValid, @Param("userInfo") UserInfo userInfo,
                                        @Param("dicTypeCode")String dicTypeCode);

    Integer getAdviceTplTotal(@Param("name") String name ,@Param("isPublic") Integer isPublic,
                                 @Param("isValid") String isValid,@Param("userInfo") UserInfo userInfo,
                                 @Param("dicTypeCode")String dicTypeCode);
}
