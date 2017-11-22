package com.acmed.his.api;

import com.acmed.his.model.Permission;
import com.acmed.his.pojo.mo.PermissionMo;
import com.acmed.his.service.PermissionManager;
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
@Api("权限管理")
@RequestMapping("/permission")
public class PermissionApi {
    @Autowired
    private PermissionManager permissionManager;

    @ApiOperation(value = "新增/编辑 权限信息")
    @PostMapping("/save")
    public ResponseResult savePermission(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PermissionMo permissionMo){
        permissionManager.save(permissionMo);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取权限列表")
    @GetMapping("/list")
    public ResponseResult<List<PermissionMo>> getPermissionList(){
        List<PermissionMo> list = new ArrayList<>();
        PermissionMo mo = new PermissionMo();
        permissionManager.getPermissionList().forEach((obj)-> {BeanUtils.copyProperties(obj,mo);list.add(mo);});
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取权限详情")
    @GetMapping("/detail")
    public ResponseResult<PermissionMo> getPermissionDetail(@ApiParam("权限主键") @RequestParam("id") Integer id){
        PermissionMo mo = new PermissionMo();
        BeanUtils.copyProperties(permissionManager.getPermissionDetail(id),mo);
        return ResponseUtil.setSuccessResult(mo);
    }

    @ApiOperation(value = "删除权限信息")
    @DeleteMapping("/del")
    public ResponseResult delPermission(@ApiParam("权限主键") @RequestParam("id") Integer id){
        permissionManager.delPermission(id);
        return ResponseUtil.setSuccessResult();
    }

}
