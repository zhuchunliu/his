package com.acmed.his.dao;

import com.acmed.his.model.Area;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AreaMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface AreaMapper extends TkMapper<Area> {
    /**
     * 保存机构时候，根据机构地址动态获取区域信息【仅仅区域用，其他地方勿用】
     * @param address
     * @return
     */
    List<Area> getOrgAddress(@Param("address") String address);
}
