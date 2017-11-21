package com.acmed.his.dao;

import com.acmed.his.model.Role;
import com.acmed.his.model.User;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface UserMapper extends TkMapper<User> {
    User getUserByLoginNameOrPhone(@Param("loginName") String loginName);

    List<Role> getRoleByUser(Integer uid);
}
