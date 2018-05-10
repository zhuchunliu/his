package com.acmed.his.dao;

import com.acmed.his.model.Permission;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * PermissionMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface PermissionMapper extends TkMapper<Permission> {
    List<Permission> getPermissionByRole(@Param("rid") Integer roleId);

    Integer hasPermission(@Param("uid") String uid, @Param("path") String path);

    Integer hasMenu(@Param("uid") String uid, @Param("perCode") String perCode);

    List<Permission> getBasePermission();

    List<Permission> getPermissionByPid(@Param("pid") Integer pid,@Param("orgCode") Integer orgCode);

    List<Permission> getNeedFilterPermissionList();

    List<Permission> getPermissionByUserId(@Param("uid") Integer uid,@Param("isAdmin") boolean isAdmin);
}
