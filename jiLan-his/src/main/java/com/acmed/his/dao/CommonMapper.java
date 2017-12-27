package com.acmed.his.dao;

/**
 * Created by Darren on 2017-11-20
 **/
public interface CommonMapper {
    /**
     * 根据编码前缀取得下一个编号
     * @param name
     * @return
     */
    String getNextVal(String name);
}
