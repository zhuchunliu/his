package com.acmed.his.api;

import com.acmed.his.model.Role;
import com.acmed.his.model.RoleVsPermission;
import com.acmed.his.pojo.mo.PermissionMo;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.service.RoleManager;
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
@Api("角色管理")
@RequestMapping("/role")
public class RoleApi {

    @Autowired
    private RoleManager roleManager;

    @ApiOperation(value = "新增/编辑 角色信息")
    @PostMapping("/save")
    public ResponseResult saveRole(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody RoleMo roleMo){
        roleManager.save(roleMo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取角色列表")
    @GetMapping("/list")
    public ResponseResult<List<RoleMo>> getRoleList(){
        List<RoleMo> list = new ArrayList<>();
        RoleMo roleMo = new RoleMo();
        roleManager.getRoleList().forEach((obj)->{BeanUtils.copyProperties(obj,roleMo);list.add(roleMo);});
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
    @DeleteMapping("/del")
    public ResponseResult delRole(@ApiParam("角色主键") @RequestParam("id") Integer id){
        roleManager.delRole(id);
        return ResponseUtil.setSuccessResult();
    }


    @ApiOperation(value = "获取角色绑定的权限列表")
    @GetMapping("/permission/list")
    public ResponseResult<List<PermissionMo>> getPermissionByRole(@ApiParam("角色主键") @RequestParam("rid") Integer rid) {
        List<PermissionMo> list = new ArrayList<>();
        PermissionMo mo = new PermissionMo();
        roleManager.getPermissionByRole(rid).forEach((obj) -> {
            BeanUtils.copyProperties(obj, mo);
            list.add(mo);
        });
        return ResponseUtil.setSuccessResult(list);

    }

    @ApiOperation(value = "绑定角色对应的全息信息")
    @PostMapping("/permission/add")
    public ResponseResult addRolePermission(@ApiParam("角色权限") @RequestBody RoleVsPermission roleVsPermission) {
        roleManager.addRolePermission(roleVsPermission);
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
