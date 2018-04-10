package com.acmed.his.dao;

import com.acmed.his.model.Org;
import com.acmed.his.model.dto.OrgDto;
import com.acmed.his.pojo.vo.OrgVo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * OrgMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface OrgMapper extends TkMapper<Org> {
    /**
     *
     * @param lngBegin 开始经度
     * @param lngEnd 结束经度
     * @param latBegin 开始纬度
     * @param latEnd 结束纬度
     * @param orgName 医院名称
     * @return
     */
    List<Org> getOrgList(@Param("lngBegin") double lngBegin, @Param("lngEnd")double lngEnd,
                         @Param("latBegin") double latBegin, @Param("latEnd") double latEnd,
                         @Param("orgName") String orgName);

    /**
     * 根据城市获取机构信息
     * @param cityId
     * @return
     */
    List<OrgVo> getOrgListByCity(@Param("cityId") Integer cityId, @Param("orgName") String orgName);
    List<OrgDto> getOrgDtoList(@Param("orgName") String orgName,@Param("cityId") String cityId,@Param("isRecommend") String isRecommend);
}
