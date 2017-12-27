package com.acmed.his.dao;

import com.acmed.his.model.Role;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * RoleMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface RoleMapper extends TkMapper<Role> {

    List<Role> getRoleByUser(@Param("uid") Integer uid);
}
