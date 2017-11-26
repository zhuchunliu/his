package com.acmed.his.service;

import com.acmed.his.dao.PermissionMapper;
import com.acmed.his.dao.RoleVsPermissionMapper;
import com.acmed.his.model.Permission;
import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.pojo.mo.PermissionMo;
import com.acmed.his.pojo.vo.UserInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Darren on 2017/11/21.
 */
@Service
public class PermissionManager {

    @Autowired
    private PermissionMapper permissionMapper;

    @Autowired
    private RoleVsPermissionMapper roleVsPermissionMapper;

    /**
     * 获取权限列表
     * @return
     */
    public List<Permission> getPermissionList(){
        return permissionMapper.selectAll();
    }

    /**
     * 保存/编辑权限信息
     * @param mo
     */
    public void save(PermissionMo mo,UserInfo userInfo){

        if(null == mo.getId()){
            Permission permission = new Permission();
            BeanUtils.copyProperties(mo,permission);
            permission.setOperatorUserId(userInfo.getId().toString());
            permission.setCreateTime(LocalDateTime.now().toString());
            permissionMapper.insert(permission);
        }else{
            Permission permission = permissionMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,permission);
            permission.setOperatorUserId(userInfo.getId().toString());
            permission.setModifyTime(LocalDateTime.now().toString());
            permissionMapper.updateByPrimaryKey(permission);
        }
    }

    /**
     * 获取权限详情信息
     * @param id
     * @return
     */
    public Permission getPermissionDetail(Integer id){
        return permissionMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除权限信息
     * @param id
     */
    @Transactional
    public void delPermission(Integer id){
        Example example = new Example(RoleVsPermission.class);
        example.createCriteria().andEqualTo("pid",id);
        roleVsPermissionMapper.deleteByExample(example);
        permissionMapper.deleteByPrimaryKey(id);

        // TODO:删除父权限，子权限怎么处理
    }

}
