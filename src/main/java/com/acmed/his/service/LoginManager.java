package com.acmed.his.service;

import com.acmed.his.constants.CommonConstants;
import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.RedisKeyConstants;
import com.acmed.his.dao.PatientMapper;
import com.acmed.his.dao.UserMapper;
import com.acmed.his.model.Patient;
import com.acmed.his.model.User;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.util.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import tk.mybatis.mapper.entity.Example;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Darren on 2017-11-21
 **/
@Service
public class LoginManager implements InitializingBean{
    private Logger logger = Logger.getLogger(LoginManager.class);

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private PatientMapper patientMapper;


    /**
     * 用户登录
     * @param loginName
     * @param passwd
     * @return
     * @throws Exception
     */
    public ResponseResult userlogin(String loginName, String passwd) throws Exception{
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
     * @param openid
     * @return
     */
    public RequestToken getTokenByOpenid(String openid) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = IPUtil.getIpAddr(request);

        Example example = new Example(User.class);
        example.createCriteria().andEqualTo("openid",openid);
        User user = Optional.ofNullable(userMapper.selectByExample(example)).filter((obj)->obj.size()>0).map((obj)->obj.get(0)).orElse(null);
        Patient patient = null;
        String loginid = null;

        if(null != user){
            loginid = String.format(RedisKeyConstants.USER_WEIXIN,user.getId());
        }
        if(null == user){
            new Example(Patient.class);
            example.createCriteria().andEqualTo("openid",openid);
            patient = Optional.ofNullable(patientMapper.selectByExample(example)).filter((obj)->obj.size()>0).map((obj)->obj.get(0)).orElse(null);
            if(null != patient){
                loginid = String.format(RedisKeyConstants.PATIENT_WEIXIN,patient.getId());
            }
        }
        if(null == user && null == patient){
            return new RequestToken();
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
        redisTemplate.expire(rediskey, CommonConstants.LOGININFO_WEIXIN_EXPIRE_SECONDS, TimeUnit.SECONDS);

        RequestToken requestToken = new RequestToken();
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
        User user = userMapper.getUserByLoginNameOrPhone(loginName);
        if (null == user || null == user.getId()) {
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_AUTH, "当前登录用户信息获取失败");
        }
        //redis中验证码或者数据库密码一致，即认证通过
        String validCode = Optional.ofNullable(redisTemplate.opsForValue().get(loginName)).map(Object::toString)
                .orElse("");
        String origPass = Optional.ofNullable(PassWordUtil.validate(passwd))
                .map(ResponseResult::getResult).map(Object::toString).orElse("");
        if ((StringUtils.isNotBlank(validCode) && validCode.equals(origPass)) || PassWordUtil.validate(passwd, user.getPassWd())) {
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

    @Override
    public void afterPropertiesSet() throws Exception {
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
    }


}
