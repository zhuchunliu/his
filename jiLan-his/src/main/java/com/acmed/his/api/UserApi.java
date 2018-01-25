package com.acmed.his.api;

import com.acmed.his.constants.RedisKeyConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.User;
import com.acmed.his.pojo.mo.RoleMo;
import com.acmed.his.pojo.mo.UserMo;
import com.acmed.his.pojo.mo.UserVsRoleMo;
import com.acmed.his.pojo.vo.UserInfo;
import com.acmed.his.pojo.vo.UserPatientVo;
import com.acmed.his.pojo.vo.UserVo;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.UserManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.RandomUtil;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.ResponseUtil;
import com.acmed.his.util.SmsUtil;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Darren on 2017/11/21.
 */
@RestController
@Api(tags = "用户信息",description = "用户接口/用户-角色绑定")
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserManager userManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    @Qualifier(value="stringRedisTemplate")
    private RedisTemplate redisTemplate;


    @ApiOperation(value = "新增/编辑 用户信息")
    @PostMapping("/save")
    public ResponseResult saveUser(@ApiParam("id等于null:新增; id不等于null：编辑") @RequestBody UserMo userMo,
                                   @AccessToken AccessInfo info){
        userManager.save(userMo,info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation(value = "获取用户列表")
    @GetMapping("/list")
    public ResponseResult<List<UserVo>> getUserList(@AccessToken AccessInfo info,
                                                    @Param("科室id") @RequestParam(value = "deptId",required = false) Integer deptId){
        List<UserVo> list = new ArrayList<>();
        userManager.getUserList(info.getUser(),deptId).forEach(obj->{
            UserVo userVo = new UserVo();
            BeanUtils.copyProperties(obj,userVo);
            list.add(userVo);
        });
        return ResponseUtil.setSuccessResult(list);
    }

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/detail")
    public ResponseResult<UserVo> getUserDetail(@ApiParam("用户主键 null:获取当前登录人的个人信息") @RequestParam(value = "id",required = false) Integer id,
                                                @AccessToken AccessInfo info){
        UserVo userVo = new UserVo();
        if(null == id){
            id = info.getUser().getId();
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

    @ApiOperation("修改密码")
    @PostMapping(value = "/passwd")
    public ResponseResult changePasswd(@ApiParam("{\"oldPasswd\":\"\",\"newPasswd\":\"\"},oldPasswd：老密码、newPasswd：新密码")  @RequestBody String param,
                                       @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("oldPasswd")){
            return ResponseUtil.setParamEmptyError("oldPasswd");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("newPasswd")){
            return ResponseUtil.setParamEmptyError("newPasswd");
        }
        userManager.changePasswd(JSONObject.parseObject(param).get("oldPasswd").toString(),
                JSONObject.parseObject(param).get("newPasswd").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("修改手机号")
    @PostMapping(value = "/mobile")
    public ResponseResult changeMobile(@ApiParam("{\"mobile\":\"\",\"code\":\"\"},mobile：手机号码、code：验证码")  @RequestBody String param,
                                       @AccessToken AccessInfo info){
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("mobile")){
            return ResponseUtil.setParamEmptyError("mobile");
        }
        if(org.apache.commons.lang3.StringUtils.isEmpty(param) || null == JSONObject.parseObject(param).get("code")){
            return ResponseUtil.setParamEmptyError("code");
        }
        String code = Optional.ofNullable(redisTemplate.opsForValue().get(String.format(RedisKeyConstants.USER_CODE,info.getUserId()))).
                map(obj->obj.toString()).orElse(null);
        if(StringUtils.isEmpty(code)){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"验证码已过期，请重新获取");
        }
        if(!code.equals(JSONObject.parseObject(param).get("code"))){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"验证码错误，请重新填写");
        }
        userManager.changeMobile(JSONObject.parseObject(param).get("mobile").toString(),info.getUser());
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("获取验证码")
    @GetMapping(value = "/getCode")
    public ResponseResult changeMobile(@AccessToken AccessInfo info){
        if(StringUtils.isEmpty(info.getUser().getMobile())){
            return ResponseUtil.setErrorMeg(StatusCode.FAIL,"尚未预留手机号，无法推送验证码");
        }
        String key = String.format(RedisKeyConstants.USER_CODE,info.getUserId());
        String code = Optional.ofNullable(redisTemplate.opsForValue().get(key)).map(obj->obj.toString()).
                orElse(RandomUtil.generateNumber(6));

        SmsUtil.sendSms(info.getUser().getMobile(),1,new String[]{code,"5"});
        redisTemplate.opsForValue().set(String.format(RedisKeyConstants.USER_CODE,info.getUserId()),code);
        redisTemplate.expire(String.format(RedisKeyConstants.USER_CODE,info.getUserId()),5, TimeUnit.MINUTES);
        return ResponseUtil.setSuccessResult();
    }

    @ApiOperation("根据token获取用户信息")
    @GetMapping(value = "/user")
    public ResponseResult<UserInfo> getUser(@AccessToken AccessInfo info){
        return ResponseUtil.setSuccessResult(info.getUser());
    }

    @ApiOperation("根据科室获取医生列表")
    @GetMapping(value = "/deptId")
    public ResponseResult<List<UserPatientVo>> getUserBydeptId(@ApiParam("科室id") @RequestParam("deptId") Integer deptId){
        User user = new User();
        user.setDept(deptId);
        user.setRemoved("0");
        List<User> byUser = userManager.getByUser(user);
        List<DicItem> dutys = baseInfoManager.getDicItemsByDicTypeCode("Duty");
        List<DicItem> diagnosLevels = baseInfoManager.getDicItemsByDicTypeCode("DiagnosisLevel");

        List<UserPatientVo> list = new ArrayList<>();
        for (User userItem :byUser){
            UserPatientVo userPatientVo = new UserPatientVo();
            BeanUtils.copyProperties(userItem,userPatientVo);
            for (DicItem duty : dutys){
                if (StringUtils.equals(duty.getDicItemCode(),userItem.getDuty())){
                    userPatientVo.setDutyStr(duty.getDicItemName());
                }
            }
            for (DicItem diagnosLevel : diagnosLevels){
                if (StringUtils.equals(diagnosLevel.getDicItemCode(),userItem.getDiagnosLevel())){
                    userPatientVo.setDiagnosLevelStr(diagnosLevel.getDicItemName());
                }
            }
            list.add(userPatientVo);
        }
        return ResponseUtil.setSuccessResult(list);
    }
}
