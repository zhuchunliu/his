package com.acmed.his.service;

import com.acmed.his.dao.PermissionMapper;
import com.acmed.his.dao.RoleMapper;
import com.acmed.his.dao.RoleVsPermissionMapper;
import com.acmed.his.model.Permission;
import com.acmed.his.model.Role;
import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.pojo.mo.RoleVsPermissionMo;
import com.acmed.his.pojo.vo.UserInfo;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
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
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("removed","0");
        return roleMapper.selectAll();
    }

    /**
     * 保存、更新角色信息
     */
    public void save(RoleMo mo, UserInfo userInfo){

        if(null == mo.getId()){
            Role role = new Role();
            BeanUtils.copyProperties(mo,role);
            role.setRemoved("0");
            role.setCreateTime(LocalDateTime.now().toString());
            role.setOperatorUserId(userInfo.getId().toString());
            roleMapper.insert(role);
        }else{
            Role role = roleMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,role);
            role.setModifyTime(LocalDateTime.now().toString());
            role.setOperatorUserId(userInfo.getId().toString());
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
    public void delRole(Integer id,UserInfo userInfo){
        Role role = roleMapper.selectByPrimaryKey(id);
        role.setModifyTime(LocalDateTime.now().toString());
        role.setOperatorUserId(userInfo.getId().toString());
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
     * @param mo
     */
    public void addRolePermission(RoleVsPermissionMo mo) {
       roleVsPermissionMapper.addRolePermission(mo.getRid(),mo.getPids().split(","));
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