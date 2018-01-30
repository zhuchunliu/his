package com.acmed.his.api;

import com.acmed.his.dao.PermissionMapper;
import com.acmed.his.model.Permission;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.pojo.vo.RoleVsPermissionVo;
import com.acmed.his.service.RoleManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.ibatis.annotations.Param;
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

    @Autowired
    private PermissionMapper permissionMapper;

    @ApiOperation(value = "新增/编辑 角色信息")
    @PostMapping("/save")
    public ResponseResult saveRole(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody RoleMo roleMo,
                                   @AccessToken AccessInfo info){
        roleManager.save(roleMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取角色列表")
    @GetMapping("/list")
    public ResponseResult<List<RoleMo>> getRoleList(@ApiParam("是否有效 0:无；1：有")@RequestParam(value = "idValid",required = false) String isValid){
        List<RoleMo> list = new ArrayList<>();

        roleManager.getRoleList(isValid).forEach(obj->{
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
    @DeleteMapping("/del")
    public ResponseResult delRole(@ApiParam("角色主键") @RequestParam("id") Integer id,
                                  @AccessToken AccessInfo info){

        roleManager.delRole(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "禁用，启用角色信息")
    @PostMapping("/switch")
    public ResponseResult switchRole(@ApiParam("{\"id\":\"\"} id：角色主键") @RequestBody String param,
                                  @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("id")){
            return ResponseUtil.setParamEmptyError("id");
        }
        roleManager.switchRole(JSONObject.parseObject(param).getInteger("id"),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "角色禁用数")
    @GetMapping("/disable/num")
    public ResponseResult getDisableNum(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(ImmutableMap.of("num",roleManager.getDisableNum()));
    }


    @ApiOperation(value = "获取角色绑定的权限列表")
    @GetMapping("/permission")
    public ResponseResult<List<RoleVsPermissionVo>> getPermissionByRole(@ApiParam("角色主键") @RequestParam(value = "rid",required = false) Integer rid) {

        List<Integer> checkedList = Lists.newArrayList();
        if(null != rid) {
            roleManager.getPermissionByRole(rid).forEach(obj -> {
                checkedList.add(obj.getId());
            });
        }

        List<Permission> parentList = permissionMapper.getBasePermission();

        List<RoleVsPermissionVo> list = Lists.newArrayList();
        parentList.forEach(parent->{

            List<Permission> childList = permissionMapper.getPermissionByPid(parent.getId());
            List<RoleVsPermissionVo.PermissionChild> childMoList = Lists.newArrayList();
            childList.forEach(child->{
                RoleVsPermissionVo.PermissionChild pchild = new RoleVsPermissionVo().new PermissionChild();
                pchild.setId(child.getId());
                pchild.setPerName(child.getPerName());
                pchild.setIsChecked(checkedList.contains(child.getId())?"1":"0");
                childMoList.add(pchild);
            });
            RoleVsPermissionVo vo = new RoleVsPermissionVo();
            vo.setPerName(parent.getPerName());
            vo.setList(childMoList);
            list.add(vo);
        });

        return ResponseUtil.setSuccessResult(list);

    }


}
