package com.acmed.his.service;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.RedisKeyConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.dao.PatientMapper;
import com.acmed.his.dao.UserMapper;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.OpenIdAndAccessToken;
import com.acmed.his.model.Patient;
import com.acmed.his.model.User;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.pojo.mo.WxUserInfo;
import com.acmed.his.util.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Darren on 2017-11-21
 **/
@Service
public class LoginManager {
    private Logger logger = Logger.getLogger(LoginManager.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    @Qualifier(value="stringRedisTemplate")
    private RedisTemplate redisTemplate;

    @Autowired
    private PatientMapper patientMapper;

    @Autowired
    private PatientManager patientManager;

    @Autowired
    private WxManager wxManager;


    /**
     * 用户登录
     * @param loginName
     * @param passwd
     * @return
     * @throws Exception
     */
    public ResponseResult<RequestToken> userlogin(String loginName, String passwd) throws Exception{
        ResponseResult result = this.validateUser(loginName, passwd);
        if(!result.isSuccess()){
            return result;
        }
        User user = (User) result.getResult();
        RequestToken requestToken = this.getToken(String.format(RedisKeyConstants.USER_PAD,user.getId()));
        logger.info("登录成功，返回的token是：" + requestToken.getToken());

        return ResponseUtil.setSuccessResult(requestToken);
    }


    /**
     * 根据openid获取token
     *
     * @param openIdAndAccessToken
     * @return
     */
    public RequestToken getTokenByOpenid(OpenIdAndAccessToken openIdAndAccessToken) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = IPUtil.getIpAddr(request);

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("openid",openIdAndAccessToken.getOpenId());
        User user = Optional.ofNullable(userMapper.selectByExample(example)).filter((obj)->obj.size()>0).map((obj)->obj.get(0)).orElse(null);

        String loginid = Optional.ofNullable(user).map(obj->String.format(RedisKeyConstants.USER_WEIXIN,user.getId())).orElse(null);

        new Example(Patient.class);
        example.createCriteria().andEqualTo("openid",openIdAndAccessToken.getOpenId());
        Patient patient = Optional.ofNullable(patientMapper.selectByExample(example)).filter((obj)->obj.size()>0).map((obj)->obj.get(0)).orElse(null);
        if(null == patient){//都没有数据的时候，则手动创建一条患者信息
            WxUserInfo wxUserInfo = null;
            try {
                wxUserInfo = wxManager.wxUserInfo(openIdAndAccessToken.getOpenId(),openIdAndAccessToken.getAccessToken());
            } catch (IOException e) {
                e.printStackTrace();
                logger.error(e.toString());
                throw new BaseException(StatusCode.FAIL,"获取微信信息失败");
            }
            patient = new Patient();
            patient.setId(UUIDUtil.generate());
            patient.setOpenid(openIdAndAccessToken.getOpenId());
            patient.setNickName(EmojiUtil.emojiConvert(wxUserInfo.getNickName()));
            patient.setAvatar(wxUserInfo.getHeadImgUrl());
            patient.setGender(wxUserInfo.getSex());
            patient.setCreateAt(LocalDateTime.now().toString());
            patientMapper.insert(patient);
            patient = patientManager.getPatientByOpenid(openIdAndAccessToken.getOpenId());
        }

        if(null == loginid){
            loginid = String.format(RedisKeyConstants.PATIENT_WEIXIN,patient.getId());
        }

        RequestToken requestToken = new RequestToken();
        if(null != user){
            requestToken.setStatus(2);
        }else {
            requestToken.setStatus(1);
        }


        //查询Redis中的Token
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        String rediskey = String.format(RedisKeyConstants.USERKEY_PRE, loginid);
        String tokenkey = RedisKeyConstants.USERTOKEN_PRE;

        Map<Object, Object> map = hash.entries(rediskey);
        if (map == null) {
            map = new HashMap<Object, Object>();
        }
        String token = (String) map.get(tokenkey);


        if (StringUtils.isEmpty(token)) {//新建token，保存redis
            token = TokenUtil.buildToken(loginid).getResult().toString();
            map.put(tokenkey, token);
            hash.putAll(rediskey,map);
        }
        redisTemplate.expire(rediskey, CommonConstants.LOGININFO_WEIXIN_EXPIRE_SECONDS, TimeUnit.DAYS);

        requestToken.setToken(token);
        requestToken.setLoginid(loginid);
        requestToken.setRemoteip(ip);

        return requestToken;

    }

    /**
     * 获取token
     * @param loginid
     * @return
     */
    public RequestToken getToken(String loginid) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = IPUtil.getIpAddr(request);

        //查询Redis中的Token
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        String rediskey = String.format(RedisKeyConstants.USERKEY_PRE, loginid);
        String tokenkey = RedisKeyConstants.USERTOKEN_PRE;

        Map<Object, Object> map = hash.entries(rediskey);
        if (map == null) {
            map = new HashMap<Object, Object>();
        }
        String token = (String) map.get(tokenkey);


        if (StringUtils.isEmpty(token)) {//新建token，保存redis
            token = TokenUtil.buildToken(loginid).getResult().toString();
            map.put(tokenkey, token);
            hash.putAll(rediskey,map);
        }
        redisTemplate.expire(rediskey, CommonConstants.LOGININFO_EXPIRE_SECONDS, TimeUnit.SECONDS);

        RequestToken requestToken = new RequestToken();
        requestToken.setToken(token);
        requestToken.setLoginid(loginid);
        requestToken.setRemoteip(ip);

        return requestToken;
    }


    /**
     * 验证用户信息
     * @param loginName
     * @param passwd
     * @return
     */
    private ResponseResult<User> validateUser(String loginName, String passwd) {
        Example example = new Example(User.class);
        example.createCriteria().orEqualTo("loginName",loginName).orEqualTo("mobile",loginName);
        userMapper.selectByExample(example);
        User user = Optional.ofNullable(userMapper.selectByExample(example)).filter(obj->0!=obj.size()).map(obj->obj.get(0)).orElse(null);
        if (null == user || null == user.getId()) {
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_AUTH, "当前登录用户信息获取失败");
        }
        if("0".equals(user.getStatus())){
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_FORBIDDEN, "当前用户已被禁用，请联系管理员");
        }
        passwd = MD5Util.encode(passwd);
        //redis中验证码或者数据库密码一致，即认证通过
        String validCode = Optional.ofNullable(redisTemplate.opsForValue().get(loginName)).map(Object::toString)
                .orElse("");
        if ((StringUtils.isEmpty(validCode) && validCode.equalsIgnoreCase(passwd)) || passwd.equalsIgnoreCase(user.getPassWd())) {
            return ResponseUtil.setSuccessResult(user);
        }

        return ResponseUtil.setErrorMeg(StatusCode.ERROR_AUTH, "用户认证失败");
    }


    public void tokenRefresh(String token) {
        String loginId = Optional.ofNullable(token)
                .map(TokenUtil::getFromToken).map(ResponseResult::getResult)
                .map(val ->(RequestToken)val).map(RequestToken::getLoginid).orElse(null);
        if(loginId.startsWith("USER_PAD"))
        redisTemplate.expire(String.format(RedisKeyConstants.USERKEY_PRE, loginId), CommonConstants.LOGININFO_EXPIRE_SECONDS, TimeUnit.SECONDS);
    }

    /**
     * 退出登录
     * @param token
     */
    public void logout(String token) {
        String loginId = Optional.ofNullable(token)
                .map(TokenUtil::getFromToken).map(ResponseResult::getResult)
                .map(val ->(RequestToken)val).map(RequestToken::getLoginid).orElse(null);
        if(!StringUtils.isEmpty(loginId)) {
            redisTemplate.opsForHash().delete(String.format(RedisKeyConstants.USERKEY_PRE, loginId), RedisKeyConstants.USERTOKEN_PRE);
        }
    }



}
