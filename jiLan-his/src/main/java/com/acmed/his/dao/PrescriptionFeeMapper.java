package com.acmed.his.dao;

import com.acmed.his.model.PrescriptionFee;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Darren on 2018-01-18
 **/
public interface PrescriptionFeeMapper extends TkMapper<PrescriptionFee> {
    void delByPreId(@Param("prescriptionId") String prescriptionId);

    void payApplyFee(@Param("applyId")String applyId , @Param("userInfo") UserInfo userInfo);

    List<PrescriptionFee> getByApplyId(@Param("applyId") String applyId);
}
