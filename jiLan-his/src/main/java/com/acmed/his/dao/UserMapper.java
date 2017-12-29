package com.acmed.his.dao;

import com.acmed.his.model.User;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * UserMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface UserMapper extends TkMapper<User> {

    User getUserByOpenid(@Param("openid") String openid);
}