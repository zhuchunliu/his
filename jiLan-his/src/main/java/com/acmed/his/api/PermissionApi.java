package com.acmed.his.api;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.pojo.mo.PermissionMo;
import com.acmed.his.service.PermissionManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
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
@Api(tags = "权限管理",hidden = true)
@RequestMapping("/permission")
public class PermissionApi {
    @Autowired
    private PermissionManager permissionManager;

    @ApiOperation(value = "新增/编辑 权限信息")
    @PostMapping("/save")
    public ResponseResult savePermission(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody PermissionMo permissionMo,
                                         @AccessToken AccessInfo info){
        permissionManager.save(permissionMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取权限列表")
    @GetMapping("/list")
    public ResponseResult<List<PermissionMo>> getPermissionList(){
        List<PermissionMo> list = new ArrayList<>();

        permissionManager.getPermissionList().forEach(obj-> {
            PermissionMo mo = new PermissionMo();
            BeanUtils.copyProperties(obj,mo);
            list.add(mo);
        });
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
        boolean flag = permissionManager.delPermission(id);
        return flag?ResponseUtil.setSuccessResult():ResponseUtil.setErrorMeg(StatusCode.FAIL,"权限删除失败，请先删除当前权限的子权限！");
    }

}
