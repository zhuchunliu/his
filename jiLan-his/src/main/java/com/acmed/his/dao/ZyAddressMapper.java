package com.acmed.his.dao;

import com.acmed.his.model.ZyAddress;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * Created by Darren on 2018-05-24
 **/
public interface ZyAddressMapper extends TkMapper<ZyAddress>{
    /**
     * 取消所有的默认配置信息
     * @param orgCode
     */
    void cancelAllDefault(@Param("orgCode") Integer orgCode);
}
