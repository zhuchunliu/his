package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.*;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.Role;
import com.acmed.his.model.User;
import com.acmed.his.model.UserVsRole;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.mo.UserVsRoleMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.MD5Util;
import com.acmed.his.util.PassWordUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 获取用户列表信息
     * @return
     */
    public List<User> getUserList(UserInfo userInfo){
        // TODO :管理员查询所有用户，规则需要待定
        if(null == userInfo.getOrgCode()){
            return userMapper.selectAll();
        }else{
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("orgCode",userInfo.getOrgCode());
            return userMapper.selectByExample(example);
        }
    }

    /**
     * 新增，编辑用户信息
     * @param mo
     */
    @Caching(evict = {
            @CacheEvict(value = "user",key = "#mo.id"),
            @CacheEvict(value="user",key="#result.openid",condition = "#result.openid ne null")
    })
    public User save(UserMo mo, UserInfo userInfo){
        //如果前端没有设置机构信息，则为当前设置用户同一机构【老板加人】；前端设置机构【管理员后台加老板用户操作】
        mo.setOrgName(null == mo.getOrgCode()?userInfo.getOrgName():
                Optional.ofNullable(orgMapper.selectByPrimaryKey(mo.getOrgCode())).map(org->org.getOrgName()).orElse(null));
        mo.setOrgCode(Optional.ofNullable(mo.getOrgCode()).orElse(userInfo.getOrgCode()));
        mo.setDeptName(null == mo.getDept()?userInfo.getDeptName():
                Optional.ofNullable(deptMapper.selectByPrimaryKey(mo.getDept())).map(dept->dept.getDept()).orElse(null));
        mo.setDept(Optional.ofNullable(mo.getDept()).orElse(userInfo.getDept()));

        mo.setPassWd(Optional.ofNullable(mo.getPassWd()).map(MD5Util::encode).orElse(null));

        if(null == mo.getId()){
            User user = new User();
            BeanUtils.copyProperties(mo,user);
            user.setCreateAt(LocalDateTime.now().toString());
            user.setCreateBy(userInfo.getId().toString());
            user.setRemoved("0");
            userMapper.insert(user);
            return user;
        }else{
            User user = userMapper.selectByPrimaryKey(mo.getId());
            BeanUtils.copyProperties(mo,user);
            user.setModifyAt(LocalDateTime.now().toString());
            user.setModifyBy(userInfo.getId().toString());
            userMapper.updateByPrimaryKey(user);
            return user;
        }

    }

    /**
     * 获取用户详情信息
     * @param id
     * @return
     */
    @Cacheable(value = "user",key = "#id")
    public User getUserDetail(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

    /**
     * 删除用户信息
     * @param id
     */
    @Caching(evict = {
            @CacheEvict(value = "user",key = "#id"),
            @CacheEvict(value="user",key="#result.openid",condition = "#result.openid ne null")
    })
    public User delUser(Integer id,UserInfo userInfo){
        User user = userMapper.selectByPrimaryKey(id);
        user.setRemoved("1");
        user.setModifyAt(LocalDateTime.now().toString());
        user.setModifyBy(userInfo.getId().toString());
        userMapper.updateByPrimaryKey(user);
        return user;
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
     * @param mo
     */
    public void addUserRole(UserVsRoleMo mo) {
        userVsRoleMapper.addUserRole(mo.getUid(), mo.getRids().split(","));
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

    /**
     * 根据openid获取用户信息
     *
     * @param openid
     * @return
     */
    @Cacheable(value="user",key = "#openid")
    public User getUserByOpenid(String openid){
        return userMapper.getUserByOpenid(openid);
    }

    @Caching(evict = {
            @CacheEvict(value = "user",key = "#result.id"),
            @CacheEvict(value="user",key="#result.openid",condition = "#result.openid ne null")
    })

    /**
     * 修改密码
     */
    public User changePasswd(String oldPasswd, String newPasswd, UserInfo userInfo) {
        User user = userMapper.selectByPrimaryKey(userInfo.getId());
        if(!user.getPassWd().equalsIgnoreCase(MD5Util.encode(oldPasswd))){
            throw new BaseException(StatusCode.ERROR_PASSWD);
        }
        user.setPassWd(MD5Util.encode(newPasswd));
        user.setModifyBy(userInfo.getId().toString());
        user.setModifyAt(LocalDateTime.now().toString());
        userMapper.updateByPrimaryKey(user);

        //TODO 删除之前密码对应的token
        return user;
    }
}
