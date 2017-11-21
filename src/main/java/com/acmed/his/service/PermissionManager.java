package com.acmed.his.service;

import com.acmed.his.dao.PermissionMapper;
import com.acmed.his.model.Permission;
import com.acmed.his.pojo.mo.PermissionMo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Darren on 2017/11/21.
 */
@Service
public class PermissionManager {

    @Autowired
    private PermissionMapper permissionMapper;

    /**
     * 获取权限列表
     * @return
     */
    public List<Permission> getPermissionList(){
        return permissionMapper.selectAll();
    }

    /**
     * 保存/编辑权限信息
     * @param permissionMo
     */
    public void save(PermissionMo permissionMo){
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionMo,permission);
        if(null == permission.getId()){
            permission.setCreateTime(new Date());
            permissionMapper.insert(permission);
        }else{
            permission.setModifyTime(new Date());
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
    public void delPermission(Integer id){
        permissionMapper.deleteByPrimaryKey(id);
    }

}
