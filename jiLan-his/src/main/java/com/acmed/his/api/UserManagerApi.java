package com.acmed.his.api;

import com.acmed.his.consts.DicTypeEnum;
import com.acmed.his.dao.RoleMapper;
import com.acmed.his.model.Role;
import com.acmed.his.model.dto.UserDto;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.mo.UserQueryMo;
import com.acmed.his.pojo.vo.UserVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.RoleManager;
import com.acmed.his.service.UserManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.PageBase;
import com.acmed.his.util.PageResult;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by Darren on 2018-01-29
 **/
@RestController
@Api(tags = "医生管理",description = "用户接口/用户-角色绑定")
@RequestMapping("/doctor")
public class UserManagerApi {

    @Autowired
    private UserManager userManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private RoleManager roleManager;

    @Autowired
    private RoleMapper roleMapper;

    @ApiOperation(value = "新增/编辑 用户信息")
    @PostMapping("/save")
    public ResponseResult saveUser(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody UserMo userMo,
                                   @AccessToken AccessInfo info){
        userManager.save(userMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户列表")
    @PostMapping("/list")
    public ResponseResult<PageResult<UserDto>> getUserList(@RequestBody(required = false) PageBase<UserQueryMo> pageBase,
                                                           @AccessToken AccessInfo info){
        PageResult result = new PageResult();
        List<UserDto> list = userManager.getUserList(pageBase.getParam(),info.getUser(),pageBase.getPageNum(),pageBase.getPageSize());
        int total = userManager.getUserTotal(pageBase.getParam(),info.getUser());
        result.setData(list);
        result.setTotal((long)total);

        return ResponseUtil.setSuccessResult(result);
    }

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/detail")
    public ResponseResult<UserVo> getUserDetail(@ApiParam("用户主键 null:获取当前登录人的个人信息") @RequestParam(value = "id",required = false) Integer id,
                                                @AccessToken AccessInfo info){
        id = Optional.ofNullable(id).orElse(info.getUserId());
        UserVo vo = new UserVo();
        BeanUtils.copyProperties(userManager.getUserDetail(id),vo);

        vo.setCategoryName(Optional.ofNullable(vo.getCategory()).
                map(obj->baseInfoManager.getDicItem(DicTypeEnum.USER_CATEGORY.getCode(),obj).getDicItemName()).orElse(null));
        vo.setDiagnosLevelName(Optional.ofNullable(vo.getDiagnosLevel()).
                map(obj->baseInfoManager.getDicItem(DicTypeEnum.DIAGNOSIS_LEVEL.getCode(),obj).getDicItemName()).orElse(null));
        vo.setDutyName(Optional.ofNullable(vo.getDuty()).
                map(obj->baseInfoManager.getDicItem(DicTypeEnum.DUTY.getCode(),obj).getDicItemName()).orElse(null));

        List<Role> roleList = roleManager.getRoleList("1");
        List<Integer> checkedRoleIdList = Lists.newArrayList();
        roleMapper.getRoleByUser(id).forEach(obj->{
            checkedRoleIdList.add(obj.getId());
        });
        List<UserVo.UserRoleVo> userRoleVoList = Lists.newArrayList();
        roleList.forEach(role->{
            UserVo.UserRoleVo userRoleVo = new UserVo().new UserRoleVo();
            userRoleVo.setRoleId(role.getId());
            userRoleVo.setRoleName(role.getRoleName());
            userRoleVo.setIsChecked(checkedRoleIdList.contains(role.getId())?"1":"0");
            userRoleVoList.add(userRoleVo);
        });
        vo.setUserRoleVoList(userRoleVoList);
        return ResponseUtil.setSuccessResult(vo);
    }

    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/del")
    public ResponseResult delUser(@ApiParam("用户主键") @RequestParam("id") Integer id,
                                  @AccessToken AccessInfo info){
        userManager.delUser(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "禁用、启用 用户")
    @PostMapping("/switch")
    public ResponseResult switchUser(@ApiParam("{\"id\":\"\"} id：用户主键") @RequestBody String param,
                                     @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        userManager.switchUser(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "禁用数")
    @GetMapping("/disable/num")
    public ResponseResult getDisableNum(@AccessToken AccessInfo info){
        UserQueryMo mo = new UserQueryMo();
        mo.setStatus("0");
        return ResponseUtil.setSuccessResult(ImmutableMap.of("num",userManager.getUserTotal(mo,info.getUser())));
    }
}
