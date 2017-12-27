package com.acmed.his.dao;

import com.acmed.his.model.UserVsRole;
import com.acmed.his.util.TkMapper;
import org.apache.ibatis.annotations.Param;

/**
 * UserVsRoleMapper
 *
 * @author jimson
 * @date 2017/11/20
 */
public interface UserVsRoleMapper extends TkMapper<UserVsRole> {
    void addUserRole(@Param("uid") Integer uid, @Param("rids") String[] rids);
}
