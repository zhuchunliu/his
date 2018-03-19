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
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    public List<Role> getRoleList(String isValid,Integer orgCode){
        Role role = new Role();
        if(!StringUtils.isEmpty(isValid)){
            role.setIsValid(isValid);
        }
//        role.setRemoved("0");
//        role.setHideFlag(0);
//        role.setOrgCode(orgCode);
        Example example = new Example(Role.class);
        if(null == orgCode){
            example.createCriteria().andEqualTo("removed","0").
                    andEqualTo("hideFlag",0).andIsNull("orgCode");
        }else{
            example.createCriteria().andEqualTo("removed","0").
                    andEqualTo("hideFlag",0).andEqualTo("orgCode",orgCode);

        }

        return roleMapper.selectByExample(example);
    }

    /**
     * 保存、更新角色信息
     */
    @Transactional
    public void save(RoleMo mo, UserInfo userInfo){
        Role role = null;
        if(null == mo.getId()){
            role = new Role();
            BeanUtils.copyProperties(mo,role);
            role.setIsValid("1");
            role.setRemoved("0");
            role.setCreateTime(LocalDateTime.now().toString());
            role.setOperatorUserId(userInfo.getId().toString());
            role.setOrgCode(userInfo.getOrgCode());
            roleMapper.insertSelective(role);
        }else{
            role = roleMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,role);
            role.setModifyTime(LocalDateTime.now().toString());
            role.setOperatorUserId(userInfo.getId().toString());
            roleMapper.updateByPrimaryKeySelective(role);
        }

        Example example = new Example(RoleVsPermission.class);
        example.createCriteria().andEqualTo("rid",role.getId());
        roleVsPermissionMapper.deleteByExample(example);
        roleVsPermissionMapper.addRolePermission(role.getId(),mo.getPids().split(","));
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

    public void switchRole(Integer id,UserInfo userInfo){
        Role role = roleMapper.selectByPrimaryKey(id);
        role.setModifyTime(LocalDateTime.now().toString());
        role.setOperatorUserId(userInfo.getId().toString());
        role.setIsValid("1".equals(role.getIsValid())?"0":"1");
        roleMapper.updateByPrimaryKey(role);
    }

    /**
     * 获取权限禁用数据
     * @return
     */
    public Integer getDisableNum(Integer orgCode) {
        Example example = new Example(Role.class);
        example.createCriteria().andEqualTo("removed","0").andEqualTo("isValid","0").andEqualTo("orgCode",orgCode);
        return roleMapper.selectCountByExample(example);

    }

    /**
     * 根据角色获取权限信息
     * @param rid
     * @return
     */
    public List<Permission> getPermissionByRole(Integer rid) {
        return permissionMapper.getPermissionByRole(rid);
    }

    @Transactional
    public Role add(Role role){
        role.setHideFlag(0);
        role.setRemoved("0");
        role.setIsValid("1");
        role.setCreateTime(LocalDateTime.now().toString());
        role.setModifyTime(null);
        roleMapper.insert(role);
        return role;
    }
}
