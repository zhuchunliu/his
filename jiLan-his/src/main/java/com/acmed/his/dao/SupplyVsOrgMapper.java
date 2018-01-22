package com.acmed.his.dao;

import com.acmed.his.model.Org;
import com.acmed.his.model.Supply;
import com.acmed.his.model.SupplyVsOrg;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * SupplyVsOrgmapper
 *
 * @author jimson
 * @date 2018/1/22
 */
public interface SupplyVsOrgMapper extends TkMapper<SupplyVsOrg>{
    List<Supply> getSupplysByOrgCode(@Param("orgCode") Integer orgCode,@Param("removed") String removed);

    List<Org> getOrgsBySupplyId(@Param("supplyId") Integer supplyId,@Param("removed") String removed);

}
