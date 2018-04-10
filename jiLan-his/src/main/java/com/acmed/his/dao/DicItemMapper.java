package com.acmed.his.dao;

import com.acmed.his.model.DicItem;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * DicItemMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface DicItemMapper extends TkMapper<DicItem> {
    List<DicItem> getDicItemsByDicTypeCode(@Param("dicTypeCode") String dicTypeCode, @Param("orgCode") Integer orgCode);
}
