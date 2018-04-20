package com.acmed.his.api;

import com.acmed.his.constants.RedisKeyConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.DicItem;
import com.acmed.his.model.Permission;
import com.acmed.his.model.User;
import com.acmed.his.pojo.mo.OpenIdAndAccessToken;
import com.acmed.his.pojo.vo.*;
import com.acmed.his.service.BaseInfoManager;
import com.acmed.his.service.PermissionManager;
import com.acmed.his.service.UserManager;
import com.acmed.his.service.WxManager;
import com.acmed.his.support.AccessInfo;
import com.acmed.his.support.AccessToken;
import com.acmed.his.util.*;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
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

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Darren on 2017/11/21.
 */
@RestController
@Api(tags = "用户信息")
@RequestMapping("/user")
public class UserApi {
    @Autowired
    private UserManager userManager;

    @Autowired
    private BaseInfoManager baseInfoManager;

    @Autowired
    private PermissionManager permissionManager;

    @Autowired
    private WxManager wxManager;

    @Autowired
    @Qualifier(value="stringRedisTemplate")
    private RedisTemplate redisTemplate;

    @ApiOperation(value = "获取用户详情")
    @GetMapping("/openid")
    public ResponseResult<UserVo> getUserDetailOpen(@ApiParam("用户主键") @RequestParam("openid") Integer openid){
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userManager.getUserByOpenid(openid.toString()),userVo);
        return ResponseUtil.setSuccessResult(userVo);
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
    public ResponseResult changeMobile(@AccessToken AccessInfo info,
                                       @Param("接受验证码的手机号") @RequestParam(value = "mobile",required = false) String mobile){
        mobile = Optional.ofNullable(mobile).orElse(info.getUser().getMobile());
        if(StringUtils.isEmpty(mobile)){
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

    @ApiOperation(value = "根据科室获取医生列表",hidden = true)
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

    @ApiOperation(value = "获取可访问菜单列表")
    @GetMapping("/menu")
    public ResponseResult<Set<MenuVo>> getMenu(@AccessToken AccessInfo info) {
        List<Permission> source = permissionManager.getPermissionByUserId(info.getUserId());
        Set<MenuVo> set = new LinkedHashSet<MenuVo>();
        source.forEach(obj->{
            set.add(new MenuVo(obj));
            if(StringUtils.isNotEmpty(obj.getPerCode()) && obj.getPerCode().equals("cgrk")){
                set.add(new MenuVo("入库审核","rksh"));
            }
            if(StringUtils.isNotEmpty(obj.getPerCode()) && obj.getPerCode().equals("kcpd")){
                set.add(new MenuVo("盘点审核","pdsh"));
            }
        });
        return ResponseUtil.setSuccessResult(set);
    }

    @ApiOperation("绑定微信")
    @GetMapping(value = "/bindwx")
    public ResponseResult getUser(@AccessToken AccessInfo info,@ApiParam("微信code") @RequestParam("code")String code){
        OpenIdAndAccessToken openid;
        try {
            openid = wxManager.getOpenid(code);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseException(StatusCode.ERROR_GETOPENIDECORD);
        }
        String openId = openid.getOpenId();
        Integer userId = info.getUserId();
        User user = new User();
        user.setId(userId);
        user.setOpenid(openId);
        userManager.changeOpenId(openId,userId,user.toString());
        return ResponseUtil.setSuccessResult();
    }
}
