package com.acmed.his.util;/**
 * Created by Eric on 2017-05-18.
 */


import com.acmed.his.constants.StatusCode;

import java.util.Map;

/**
 * 用于公共返回
 *
 * @Author Eric
 * @Version 2017-05-18 11:41
 **/

public class ResponseUtil {

    /**
     * 返回参数为空错误码
     * @param param
     * @return
     */
    public static ResponseResult setParamEmptyError(String param) {
        return getResponseResultError(null, StatusCode.ERROR_PARAM,param + " 不能为空");
    }

    /**
     * 返回密码错误提示
     * @return
     */
    public static ResponseResult setPasswordError() {
        return getResponseResultError(null,StatusCode.ERROR_PASSWD,"密码错误");
    }

    /**
     * 返回成功提示
     * @param content
     * @return
     */
    public static ResponseResult setSuccessResult(Object content) {
        return getResponseMsg(content,true,StatusCode.SUCCESS,"成功");
    }

    /**
     * 返回成功提示
     * @author Issac
     * @return ResponseResult
     */
    public static ResponseResult setSuccessResult() {
        return getResponseMsg(null,true,StatusCode.SUCCESS,"成功");
    }

    /**
     * 入参错误
     * @return
     */
    public static ResponseResult setParamError() {
        return getResponseResultError(null,StatusCode.ERROR_PARAM,"入参错误");
    }

    /**
     * 刷新Token错误
     * @return
     */
    public static ResponseResult setRefreshTokenError(String msg) {
        return getResponseResultError(null,StatusCode.ERROR_REFRESHTOKEN,"RefreshToken 错误");
    }


    /**
     * 入参错误
     * @return
     */
    public static ResponseResult setErrorMeg(StatusCode statusCode,String  msg) {
        return getResponseResultError(null,statusCode,msg);
    }


    /**
     * 返回错误信息
     * @param param
     * @param errorCode
     * @param msg
     * @return
     */
    private static ResponseResult getResponseResultError(String param,StatusCode errorCode , String msg) {
        return  getResponseMsg(param,false,errorCode,msg);
    }

    /**
     * 返回信息
     * @param param
     * @param isSuccess
     * @param errorCode
     * @param msg
     * @return
     */
    private static ResponseResult getResponseMsg(Object param,boolean isSuccess , StatusCode errorCode , String msg) {
        ResponseResult result = new ResponseResult();

        result.setSuccess(isSuccess);
        result.setStatusCode(errorCode);
        result.setMsg(msg);

        //设置内容
        result.setResult(param);
        return result;
    }

    /**
     * 参数非空校验
     * @param paramMap
     * @return
     */
    public static ResponseResult getParamEmptyError(Map<String,Object> paramMap) {
        if(paramMap != null && paramMap.size() > 0){
            for(Map.Entry<String,Object> entry : paramMap.entrySet()){
                if(EmptyUtil.isEmpty(entry.getValue())){
                    return ResponseUtil.setParamEmptyError(entry.getKey());
                }
            }
        }
        return null;
    }
}
