package com.acmed.his.util;


import com.acmed.his.constants.StatusCode;
import com.acmed.his.constants.AppConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by Darren on 2017-11-21
 */
public class PassWordUtil {
    private static Logger logger = LoggerFactory.getLogger(PassWordUtil.class);

    /**
     * 根据passwd,返回加密密码数据
     *
     * @param passwd
     * @return
     */
    public static ResponseResult encode(String passwd) {
        Date date = new Date();
        String timestamp = String.valueOf(date.getTime());
        String random = RandomUtil.generateString(5);
        String tmp = String.format(AppConstants.PASSWORD_BUILD, random, passwd, timestamp);
        byte[] data = tmp.getBytes();
        try {
            tmp = Base64Utils.encode(data);
            //反转
            StringBuilder sb = new StringBuilder(tmp);
            String str= sb.reverse().toString();
            logger.debug("加密:\r" + str);
            return  ResponseUtil.setSuccessResult(str);

        } catch (Exception e) {
            return ResponseUtil.setErrorMeg(StatusCode.ERROR_PASSWD,"密码生成出错");
        }
    }

    public static ResponseResult  validate(String str) {
        ResponseResult result=new ResponseResult();
        String tmp=decode(str);
        tmp=tmp.substring(5,tmp.length()-13);
        //logger.debug("---------------key" + tmp);
        result=ResponseUtil.setSuccessResult(tmp);
        return result;
    }

    /**
     * 校验两者密码是否相同
     * @author Issac
     * @param srcPwd
     * @param dbPwd
     * @return
     */
    public static boolean validate(String srcPwd, String dbPwd) {
        ResponseResult srcRs = PassWordUtil.validate(srcPwd);
        ResponseResult dbRs = PassWordUtil.validate(dbPwd);
        if (srcRs.isSuccess() && dbRs.isSuccess()) {
            return srcRs.getResult().toString().equals(dbRs.getResult().toString());
        }
        return false;
    }

    /**
     * 根据加密后的密码，返回原始值
     * @param str
     * @return
     */
    private static String decode(String str) {
        String result = null;
        try {
            //反转
            StringBuilder sb = new StringBuilder(str);
            String tmp = sb.reverse().toString();
            result = new String(Base64Utils.decode(tmp));
            logger.debug("验证结果:\r" + result);
        } catch (Exception ex) {
        }
        return result;
    }

    public static void main(String[] args) {
        System.out.println(encode("123456a"));
    }

}
