package com.acmed.his.service;

import com.acmed.his.dao.RoleMapper;
import com.acmed.his.dao.UserMapper;
import com.acmed.his.dao.UserVsRoleMapper;
import com.acmed.his.model.Role;
import com.acmed.his.model.User;
import com.acmed.his.model.UserVsRole;
import com.acmed.his.pojo.mo.UserMo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by Darren on 2017/11/21.
 */
@Service
public class UserManager {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private UserVsRoleMapper userVsRoleMapper;

    /**
     * 获取用户列表信息
     * @return
     */
    public List<User> getUserList(){
        return userMapper.selectAll();
    }

    /**
     * 新增，编辑用户信息
     * @param userMo
     */
    public void save(UserMo userMo){
        User user = new User();
        BeanUtils.copyProperties(userMo,user);
        user.setCreateAt(new Date());
        user.setRemoved("0");
        if(null == user.getId()){
            userMapper.insert(user);
        }else{
            userMapper.updateByPrimaryKey(user);
        }
    }

    /**
     * 获取用户详情信息
     * @param id
     * @return
     */
    public User getUserDetail(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除用户信息
     * @param id
     */
    public void delUser(Integer id){
        User user = userMapper.selectByPrimaryKey(id);
        user.setRemoved("1");
        userMapper.updateByPrimaryKey(user);
    }

    /**
     * 获取用户绑定的权限信息
     * @param uid
     * @return
     */
    public List<Role> getRoleByUser(Integer uid) {
        return  roleMapper.getRoleByUser(uid);
    }

    /**
     * 绑定用户权限信息
     * @param userVsRole
     */
    public void addUserRole(UserVsRole userVsRole) {
        userVsRoleMapper.insert(userVsRole);
    }

    /**
     * 删除用户权限信息
     * @param rid
     * @param uid
     */
    public void delUserRole(Integer uid, Integer rid) {
        UserVsRole userVsRole = new UserVsRole();
        userVsRole.setRid(rid);
        userVsRole.setUid(uid);
        userVsRoleMapper.delete(userVsRole);
    }
}
