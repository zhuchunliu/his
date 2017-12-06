package com.acmed.his.api;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.mo.UserVsRoleMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.vo.UserVo;
import com.acmed.his.service.UserManager;
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
@Api(tags = "用户信息",description = "用户接口/用户-角色绑定")
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserManager userManager;

    @ApiOperation(value = "新增/编辑 用户信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @PostMapping("/save")
    public ResponseResult saveUser(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody UserMo userMo,
                                   @AccessToken AccessInfo info){
        userManager.save(userMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户列表")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping("/list")
    public ResponseResult<List<UserVo>> getUserList(@AccessToken AccessInfo info){
        List<UserVo> list = new ArrayList<>();
        userManager.getUserList(info.getUser()).forEach(obj->{
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(obj,userVo);
            list.add(userVo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取用户详情")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @GetMapping("/detail")
    public ResponseResult<UserVo> getUserDetail(@ApiParam("用户主键 null:获取当前登录人的个人信息") @RequestParam(value = "id",required = false) Integer id,
                                                @AccessToken UserInfo userInfo){
        UserVo userVo = new UserVo();
        if(null == id){
            id = userInfo.getId();
        }
        BeanUtils.copyProperties(userManager.getUserDetail(id),userVo);
        return ResponseUtil.setSuccessResult(userVo);
    }

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/openid")
    public ResponseResult<UserVo> getUserDetailOpen(@ApiParam("用户主键") @RequestParam("openid") Integer openid){
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userManager.getUserByOpenid(openid.toString()),userVo);
        return ResponseUtil.setSuccessResult(userVo);
    }

    @ApiOperation(value = "删除用户信息")
    @ApiImplicitParam(paramType = "header", dataType = "String", name = CommonConstants.USER_HEADER_TOKEN, value = "token", required = true)
    @DeleteMapping("/del")
    public ResponseResult delUser(@ApiParam("用户主键") @RequestParam("id") Integer id,
                                  @AccessToken AccessInfo info){
        userManager.delUser(id,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户角色列表")
    @GetMapping("/role/list")
    public ResponseResult<List<RoleMo>> getPermissionByRole(@ApiParam("用户主键") @RequestParam("uid") Integer uid) {
        List<RoleMo> list = new ArrayList<>();

        userManager.getRoleByUser(uid).forEach(obj->{
            RoleMo roleMo = new RoleMo();
            BeanUtils.copyProperties(obj,roleMo);
            list.add(roleMo);
        });
        return ResponseUtil.setSuccessResult(list);

    }

    @ApiOperation(value = "绑定用户对于的角色信息")
    @PostMapping("/role/add")
    public ResponseResult addRolePermission(@ApiParam("用户角色") @RequestBody UserVsRoleMo mo) {
        userManager.addUserRole(mo);
        return ResponseUtil.setSuccessResult();

    }

    @ApiOperation(value = "删除用户绑定的角色信息")
    @DeleteMapping("/role/del")
    public ResponseResult delRolePermission(@ApiParam("用户主键") @RequestParam("uid") Integer uid,
                                            @ApiParam("角色主键") @RequestParam("rid") Integer rid) {
        userManager.delUserRole(uid,rid);
        return ResponseUtil.setSuccessResult();

    }

}
