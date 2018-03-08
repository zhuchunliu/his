package com.acmed.his.service;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.*;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.DicType;
import com.acmed.his.model.Role;
import com.acmed.his.model.User;
import com.acmed.his.model.UserVsRole;
import com.acmed.his.model.dto.UserDto;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.mo.UserQueryMo;
import com.acmed.his.pojo.mo.UserVsRoleMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.util.IdCardUtil;
import com.acmed.his.util.MD5Util;
import com.acmed.his.util.PassWordUtil;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
    private UserVsRoleMapper userVsRoleMapper;

    @Autowired
    private OrgMapper orgMapper;

    @Autowired
    private DeptMapper deptMapper;

    /**
     * 获取用户列表信息
     * @return
     */
    public List<UserDto> getUserList(UserQueryMo mo, UserInfo userInfo, Integer pageNum , Integer pageSize){
        PageHelper.startPage(pageNum,pageSize);
        return userMapper.getUserList(Optional.ofNullable(mo).map(obj->obj.getDeptId()).orElse(null),
                Optional.ofNullable(mo).map(obj->obj.getMobile()).orElse(null),
                Optional.ofNullable(mo).map(obj->obj.getUserName()).orElse(null),
                Optional.ofNullable(mo).map(obj->obj.getStatus()).orElse(null),userInfo.getOrgCode(),
                DicTypeEnum.USER_CATEGORY.getCode(),DicTypeEnum.DIAGNOSIS_LEVEL.getCode(),
                DicTypeEnum.DUTY.getCode());
    }

    public int getUserTotal(UserQueryMo mo, UserInfo userInfo) {
        return userMapper.getUserTotal(Optional.ofNullable(mo).map(obj->obj.getDeptId()).orElse(null),
                Optional.ofNullable(mo).map(obj->obj.getMobile()).orElse(null),
                Optional.ofNullable(mo).map(obj->obj.getUserName()).orElse(null),
                Optional.ofNullable(mo).map(obj->obj.getStatus()).orElse(null),userInfo.getOrgCode());
    }

    /**
     * 新增，编辑用户信息
     * @param mo
     */
    @Caching(evict = {
            @CacheEvict(value = "user",key = "#mo.id"),
            @CacheEvict(value="user",key="#result.openid",condition = "#result.openid ne null")
    })
    @Transactional
    public User save(UserMo mo, UserInfo userInfo,Boolean orgSource){

        //如果前端没有设置机构信息，则为当前设置用户同一机构【老板加人】；前端设置机构【管理员后台加老板用户操作】
        mo.setOrgCode(Optional.ofNullable(mo.getOrgCode()).orElse(userInfo.getOrgCode()));
        mo.setDept(Optional.ofNullable(mo.getDept()).orElse(userInfo.getDept()));

        if(!StringUtils.isEmpty(mo.getLoginName()) ) { //验证手机号，用户名是否已经存在
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("loginName",mo.getLoginName());
            Integer id = Optional.ofNullable(userMapper.selectByExample(example)).
                    filter(obj->0!=obj.size()).map(obj->obj.get(0)).map(obj->obj.getId()).orElse(null);
            if((null != id && null == mo.getId())  || (null != id && id != mo.getId())){
                throw new BaseException(StatusCode.FAIL,orgSource?"医院名不能重复":"登录名不能重复");
            }
        }

        if(!StringUtils.isEmpty(mo.getMobile())){
            Example example = new Example(User.class);
            example.createCriteria().andEqualTo("mobile",mo.getMobile());
            Integer id = Optional.ofNullable(userMapper.selectByExample(example)).
                    filter(obj->0!=obj.size()).map(obj->obj.get(0)).map(obj->obj.getId()).orElse(null);
            if((null != id && null == mo.getId())  || (null != id && id != mo.getId())){
                throw new BaseException(StatusCode.FAIL,"手机号不能重复");
            }
        }

        User user = null;
        if(null == mo.getId()){
            user = new User();
            BeanUtils.copyProperties(mo,user);
            user.setStatus("1");
            user.setCreateAt(LocalDateTime.now().toString());
            user.setCreateBy(userInfo.getId().toString());
            user.setOrgName(null == mo.getOrgCode()?userInfo.getOrgName():
                    Optional.ofNullable(orgMapper.selectByPrimaryKey(mo.getOrgCode())).map(org->org.getOrgName()).orElse(null));
            user.setDeptName(null == mo.getDept()?userInfo.getDeptName():
                    Optional.ofNullable(deptMapper.selectByPrimaryKey(mo.getDept())).map(dept->dept.getDept()).orElse(null));
            user.setPassWd(MD5Util.encode("123456"));
            user.setRemoved("0");
            if(StringUtils.isNotEmpty(mo.getDateOfBirth())){
                user.setAge(IdCardUtil.idCardToAge("000000"+mo.getDateOfBirth().replace("-","")+"0000"));
            }
            userMapper.insert(user);
        }else{
            user = userMapper.selectByPrimaryKey(mo.getId());
            user.setOrgName(null == mo.getOrgCode()?userInfo.getOrgName():
                    Optional.ofNullable(orgMapper.selectByPrimaryKey(mo.getOrgCode())).map(org->org.getOrgName()).orElse(null));
            user.setDeptName(null == mo.getDept()?userInfo.getDeptName():
                    Optional.ofNullable(deptMapper.selectByPrimaryKey(mo.getDept())).map(dept->dept.getDept()).orElse(null));
            BeanUtils.copyProperties(mo,user);
            user.setModifyAt(LocalDateTime.now().toString());
            user.setModifyBy(userInfo.getId().toString());
            if(StringUtils.isNotEmpty(mo.getDateOfBirth())){
                user.setAge(IdCardUtil.idCardToAge("000000"+mo.getDateOfBirth().replace("-","")+"0000"));
            }
            userMapper.updateByPrimaryKey(user);
        }

        Example example = new Example(UserVsRole.class);
        example.createCriteria().andEqualTo("rid",user.getId());
        userVsRoleMapper.deleteByExample(example);
        if(StringUtils.isNotEmpty(mo.getRoleIds())) {
            userVsRoleMapper.addUserRole(user.getId(), mo.getRoleIds().split(","));
        }
        return user;

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
     * 禁用、启用用户信息
     * @param id
     * @param userInfo
     * @return
     */
    @Caching(evict = {
            @CacheEvict(value = "user",key = "#id"),
            @CacheEvict(value="user",key="#result.openid",condition = "#result.openid ne null")
    })
    public User switchUser(Integer id,UserInfo userInfo){
        User user = userMapper.selectByPrimaryKey(id);
        user.setStatus(user.getStatus().equals("1")?"0":"1");
        user.setModifyAt(LocalDateTime.now().toString());
        user.setModifyBy(userInfo.getId().toString());
        userMapper.updateByPrimaryKey(user);
        return user;
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



    /**
     * 修改密码
     */
    @Caching(evict = {
            @CacheEvict(value = "user",key = "#result.id"),
            @CacheEvict(value="user",key="#result.openid",condition = "#result.openid ne null")
    })
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


    @Caching(evict = {
            @CacheEvict(value = "user",key = "#result.id"),
            @CacheEvict(value="user",key="#result.openid",condition = "#result.openid ne null")
    })
    public User changeMobile(String mobile, UserInfo userInfo) {
        User user = userMapper.selectByPrimaryKey(userInfo.getId());
        user.setMobile(mobile);
        user.setModifyBy(userInfo.getId().toString());
        user.setModifyAt(LocalDateTime.now().toString());
        userMapper.updateByPrimaryKey(user);
        return user;
    }

    public List<User> getByUser(User user){
        return userMapper.select(user);
    }



}
