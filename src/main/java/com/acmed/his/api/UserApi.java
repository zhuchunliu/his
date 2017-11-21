package com.acmed.his.api;

import com.acmed.his.model.Role;
import com.acmed.his.model.User;
import com.acmed.his.model.UserVsRole;
import com.acmed.his.service.UserManager;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public ResponseResult saveUser(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody User user){
        userManager.save(user);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public ResponseResult<List<User>> getUserList(){
        return ResponseUtil.setSuccessResult(userManager.getUserList());
    }

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/detail")
    public ResponseResult<User> getUserDetail(@ApiParam("用户主键") @RequestParam("id") Integer id){
        return ResponseUtil.setSuccessResult(userManager.getUserDetail(id));
    }

    @ApiOperation(value = "删除用户信息")
    @DeleteMapping("/del")
    public ResponseResult delUser(@ApiParam("用户主键") @RequestParam("id") Integer id){
        userManager.delUser(id);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户权限列表")
    @GetMapping("/permission/list")
    public ResponseResult<List<Role>> getPermissionByRole(@ApiParam("用户主键") @RequestParam("uid") Integer uid) {

        return ResponseUtil.setSuccessResult(userManager.getRoleByUser(uid));

    }

    @ApiOperation(value = "绑定用户对于的权限信息")
    @PostMapping("/permission/add")
    public ResponseResult addRolePermission(@ApiParam("用户角色") @RequestBody UserVsRole userVsRole) {
        userManager.addUserRole(userVsRole);
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value = "删除用户绑定的权限信息")
    @DeleteMapping("/permission/del")
    public ResponseResult delRolePermission(@ApiParam("角色主键") @RequestParam("rid") Integer rid,
                                            @ApiParam("用户主键") @RequestParam("uid") Integer uid) {
        userManager.delUserRole(rid,uid);
        return ResponseUtil.setSuccessResult();

    }

}
