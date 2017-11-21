package com.acmed.his.util;/**
 * Created by Eric on 2017-05-19.
 */

import com.acmed.his.constants.StatusCode;
import com.acmed.his.consts.AppConstants;
import com.acmed.his.pojo.RequestToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Token管理类
 *
 * @Author Eric
 * @Version 2017-05-19 9:24
 **/
public class TokenUtil {

    private static Logger logger = LoggerFactory.getLogger(TokenUtil.class);

    /**
     * 生成token
     *
     * @param id
     * @return
     */
    public static ResponseResult buildToken(String id) {
        ResponseResult result = new ResponseResult();
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        String random = RandomUtil.generateString(20);
        String randomend = RandomUtil.generateString(1);
        timestamp = timestamp + random;
        String tmp = String.format(AppConstants.TOKEN_BUILD, id, timestamp);
        logger.debug(timestamp.length() + "---------------原始" + tmp);
        byte[] data = tmp.getBytes();
        try {
            tmp = Base64Utils.encode(data);
            //logger.debug("tmp:\r" + tmp);
            //反转
            StringBuilder sb = new StringBuilder(tmp);

            //签名加一位随机数
            String token = randomend + sb.reverse().toString();
            //logger.debug("签名:\r" + token);
            result = ResponseUtil.setSuccessResult(token);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
            logger.error("---------------error" + e.getMessage());
            result = ResponseUtil.setErrorMeg(StatusCode.FAIL, "token生成出错");
        }
        return result;
    }


    /**
     * 解析token，返回原始数据
     *
     * @param token
     * @return
     */
    private static ResponseResult reverseToken(String token) {
        ResponseResult result = new ResponseResult();
        try {
            //去除第一位
            token = token.substring(1);
            //反转
            StringBuilder sb = new StringBuilder(token);
            String tmp = sb.reverse().toString();
            tmp = new String(Base64Utils.decode(tmp));
            //logger.debug("验证结果:\r" + tmp);
            result = ResponseUtil.setSuccessResult(tmp);
        } catch (Exception ex) {
            logger.error("---------------error" + ex.getMessage());
            result = ResponseUtil.setErrorMeg(StatusCode.FAIL, "token解析出错");
        }
        return result;
    }


    public static ResponseResult getFromToken(String token) {
        ResponseResult result = new ResponseResult();
        try {
            String category, loginid;
            result = reverseToken(token);
            String tmp = (String) result.getResult();
//            category = tmp.substring(0, 1);
            //去掉第一个和后面时间戳和随机数
            loginid = tmp.substring(0, tmp.length() - 33);
            RequestToken requestToken = new RequestToken();
            requestToken.setLoginid(loginid);
            requestToken.setToken(token);
            result = ResponseUtil.setSuccessResult(requestToken);
            return result;
        } catch (Exception ex) {
            logger.error("---------------error" + ex.getMessage());
            result = ResponseUtil.setErrorMeg(StatusCode.FAIL, "token解析出错");
            return result;
        }
    }

    public static void main(String args[]) throws Exception{
        String token = null;
        ResponseResult res = TokenUtil.buildToken("1234569890");
        if (res.isSuccess()) {
            token = (String) res.getResult();
            System.err.println("---------------签名" + token);
        }
        res = TokenUtil.reverseToken(token);
        String str = (String) res.getResult();
        System.err.println("---------------验证" + str);


    }
}
