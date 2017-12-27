package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.pojo.mo.PermissionMo;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.pojo.mo.RoleVsPermissionMo;
import com.acmed.his.service.RoleManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
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
@Api(tags = "角色管理",description = "角色设定/角色-权限绑定")
@RequestMapping("/role")
public class RoleApi {

    @Autowired
    private RoleManager roleManager;

    @ApiOperation(value = "新增/编辑 角色信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/save")
    public ResponseResult saveRole(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody RoleMo roleMo,
                                   @AccessToken AccessInfo info){
        roleManager.save(roleMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取角色列表")
    @GetMapping("/list")
    public ResponseResult<List<RoleMo>> getRoleList(){
        List<RoleMo> list = new ArrayList<>();

        roleManager.getRoleList().forEach(obj->{
            RoleMo roleMo = new RoleMo();
            BeanUtils.copyProperties(obj,roleMo);
            list.add(roleMo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取角色详情")
    @GetMapping("/detail")
    public ResponseResult<RoleMo> getRoleDetail(@ApiParam("角色主键") @RequestParam("id") Integer id){
        RoleMo roleMo = new RoleMo();
        BeanUtils.copyProperties(roleManager.getRoleDetail(id),roleMo);
        return ResponseUtil.setSuccessResult(roleMo);
    }

    @ApiOperation(value = "删除角色信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/del")
    public ResponseResult delRole(@ApiParam("角色主键") @RequestParam("id") Integer id,
                                  @AccessToken AccessInfo info){
        roleManager.delRole(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "获取角色绑定的权限列表")
    @GetMapping("/permission/list")
    public ResponseResult<List<PermissionMo>> getPermissionByRole(@ApiParam("角色主键") @RequestParam("rid") Integer rid) {
        List<PermissionMo> list = new ArrayList<>();

        roleManager.getPermissionByRole(rid).forEach(obj -> {
            PermissionMo mo = new PermissionMo();
            BeanUtils.copyProperties(obj, mo);
            list.add(mo);
        });
        return ResponseUtil.setSuccessResult(list);

    }

    @ApiOperation(value = "绑定角色对应的全息信息")
    @PostMapping("/permission/add")
    public ResponseResult addRolePermission(@ApiParam("角色权限") @RequestBody RoleVsPermissionMo mo) {
        roleManager.addRolePermission(mo);
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value = "删除角色绑定的权限信息")
    @DeleteMapping("/permission/del")
    public ResponseResult delRolePermission(@ApiParam("角色主键") @RequestParam("rid") Integer rid,
                                            @ApiParam("权限主键") @RequestParam("pid") Integer pid) {
        roleManager.delRolePermission(rid,pid);
        return ResponseUtil.setSuccessResult();

    }
}
