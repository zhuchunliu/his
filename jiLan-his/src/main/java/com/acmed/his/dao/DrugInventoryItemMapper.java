package com.acmed.his.dao;

import com.acmed.his.model.DrugInventoryItem;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-03-13
 **/
public interface DrugInventoryItemMapper extends TkMapper<DrugInventoryItem> {
    void delItemNotInIds(@Param("ids") List<String> itemIds, @Param("inventoryId") String inventoryid);
}
