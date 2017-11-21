package com.acmed.his.service;

import com.acmed.his.dao.PermissionMapper;
import com.acmed.his.dao.RoleMapper;
import com.acmed.his.dao.RoleVsPermissionMapper;
import com.acmed.his.model.Permission;
import com.acmed.his.model.Role;
import com.acmed.his.model.RoleVsPermission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Darren on 2017/11/21.
 */
@Service
public class RoleManager {
    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleVsPermissionMapper roleVsPermissionMapper;

    /**
     * 获取角色列表
     * @return
     */
    public List<Role> getRoleList(){
        return roleMapper.selectAll();
    }

    /**
     * 保存、更新角色信息
     * @param role
     */
    public void save(Role role){
        if(null == role.getId()){
            roleMapper.insert(role);
        }else{
            roleMapper.updateByPrimaryKey(role);
        }
    }

    /**
     * 获取角色详情
     * @param id
     * @return
     */
    public Role getRoleDetail(Integer id){
        return roleMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除角色信息
     * @param id
     */
    public void delRole(Integer id){
        roleMapper.deleteByPrimaryKey(id);
    }


    /**
     * 根据角色获取权限信息
     * @param rid
     * @return
     */
    public List<Permission> getPermissionByRole(Integer rid) {
        return permissionMapper.getPermissionByRole(rid);
    }

    /**
     * 绑定角色对应的权限信息
     * @param roleVsPermission
     */
    public void addRolePermission(RoleVsPermission roleVsPermission) {
        roleVsPermissionMapper.insert(roleVsPermission);
    }

    /**
     * 删除角色对应的权限信息
     * @param rid
     * @param pid
     */
    public void delRolePermission(Integer rid, Integer pid) {
        RoleVsPermission roleVsPermission = new RoleVsPermission();
        roleVsPermission.setPid(pid);
        roleVsPermission.setRid(rid);
        roleVsPermissionMapper.delete(roleVsPermission);
    }
}
