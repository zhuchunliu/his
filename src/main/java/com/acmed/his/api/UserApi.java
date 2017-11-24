package com.acmed.his.api;

import com.acmed.his.model.UserVsRole;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.service.UserManager;
import com.acmed.his.support.AccessToken;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Darren on 2017/11/21.
 */
@RestController
@Api("用户信息")
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserManager userManager;

    @ApiOperation(value = "新增/编辑 用户信息")
    @PostMapping("/save")
    public ResponseResult saveUser(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody UserMo userMo){
        userManager.save(userMo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public ResponseResult<List<UserMo>> getUserList(@AccessToken AccessInfo accessInfo){
        List<UserMo> list = new ArrayList<>();
        UserMo userMo = new UserMo();
        userManager.getUserList().forEach((obj)->{
            BeanUtils.copyProperties(obj,userMo);list.add(userMo);});
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/detail")
    public ResponseResult<UserMo> getUserDetail(@ApiParam("用户主键") @RequestParam("id") Integer id){
        UserMo userMo = new UserMo();
        BeanUtils.copyProperties(userManager.getUserDetail(id),userMo);
        return ResponseUtil.setSuccessResult(userMo);
    }

    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/del")
    public ResponseResult delUser(@ApiParam("用户主键") @RequestParam("id") Integer id){
        userManager.delUser(id);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户权限列表")
    @GetMapping("/permission/list")
    public ResponseResult<List<RoleMo>> getPermissionByRole(@ApiParam("用户主键") @RequestParam("uid") Integer uid) {
        List<RoleMo> list = new ArrayList<>();
        RoleMo roleMo = new RoleMo();
        userManager.getRoleByUser(uid).forEach((obj)->{BeanUtils.copyProperties(obj,roleMo);list.add(roleMo);});
        return ResponseUtil.setSuccessResult(list);

    }

    @ApiOperation(value = "绑定用户对于的权限信息")
    @PostMapping("/permission/add")
    public ResponseResult addRolePermission(@ApiParam("用户角色") @RequestBody UserVsRole userVsRole) {
        userManager.addUserRole(userVsRole);
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value = "删除用户绑定的权限信息")
    @DeleteMapping("/permission/del")
    public ResponseResult delRolePermission(@ApiParam("用户主键") @RequestParam("uid") Integer uid,
                                            @ApiParam("角色主键") @RequestParam("rid") Integer rid) {
        userManager.delUserRole(uid,rid);
        return ResponseUtil.setSuccessResult();

    }

}
