package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionTplItem;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2017-11-20
 **/
public interface PrescriptionTplItemMapper extends TkMapper<PrescriptionTplItem>{
    List<PrescriptionTplItem> getPrescripTplItemList(@Param("tplId") Integer tplId);

    void deleteByTplId(@Param("tplId") Integer id);
}
