package com.acmed.his.service;

import com.acmed.his.dao.PermissionMapper;
import com.acmed.his.dao.RoleMapper;
import com.acmed.his.dao.RoleVsPermissionMapper;
import com.acmed.his.model.Permission;
import com.acmed.his.model.Role;
import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.pojo.mo.RoleMo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
     */
    public void save(RoleMo roleMo){
        Role role = new Role();
        BeanUtils.copyProperties(roleMo,role);
        role.setRemoved("0");
        role.setCreateTime(new Date());
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
        Role role = roleMapper.selectByPrimaryKey(id);
        role.setRemoved("1");
        roleMapper.updateByPrimaryKey(role);
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
