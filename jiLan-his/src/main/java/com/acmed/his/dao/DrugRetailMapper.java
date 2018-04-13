package com.acmed.his.dao;

import com.acmed.his.model.DrugRetail;
import com.acmed.his.model.dto.DrugRetailDto;
import com.acmed.his.pojo.mo.DrugRetailQueryMo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-04-13
 **/
public interface DrugRetailMapper extends TkMapper<DrugRetail> {
    /**
     * 零售列表
     * @param mo
     */
    List<DrugRetailDto> getRetailList(@Param("mo") DrugRetailQueryMo mo, @Param("orgCode") Integer orgCode);
}
