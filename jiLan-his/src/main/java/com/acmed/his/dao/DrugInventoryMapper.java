package com.acmed.his.dao;

import com.acmed.his.model.DrugInventory;
import com.acmed.his.model.dto.DrugInventoryDto;
import com.acmed.his.pojo.mo.DrugInventoryQueryMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-03-13
 **/
public interface DrugInventoryMapper extends TkMapper<DrugInventory> {
    List<DrugInventoryDto> getAuditList(@Param("userInfo") UserInfo user, @Param("mo") DrugInventoryQueryMo mo,
                                        @Param("hasPermission") Boolean hasPermission);

    Integer getAuditTotal(@Param("userInfo") UserInfo user, @Param("mo") DrugInventoryQueryMo mo,
                          @Param("hasPermission") Boolean hasPermission);
}
