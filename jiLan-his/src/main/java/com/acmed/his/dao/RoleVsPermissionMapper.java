package com.acmed.his.dao;

import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * RoleVsPermissionMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface RoleVsPermissionMapper extends TkMapper<RoleVsPermission> {
    void addRolePermission(@Param("rid") Integer rid,@Param("pids") String[] pid);

    void init(@Param("rid") Integer rid);
}
