package com.acmed.his.util;

import com.acmed.his.constants.StatusCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.Serializable;

/**
 * reponse公共类
 *
 * @Author Eric
 * @Version 2017-05-18 11:15
 **/


@Log4j
public @Data
class ResponseResult<T> implements Serializable {
    @ApiModelProperty(value="返回状态 success / error")
    private  boolean success=false;//返回状态：success / error
    @ApiModelProperty(value="返回状态代码")
    private StatusCode statusCode;//状态码: 参见StatusCode
    @ApiModelProperty(value="返回消息")
    private String msg;
    @ApiModelProperty(value="返回结果")
    private T result;

    public boolean isSuccess()
    {
        return  success;
    }

}
