package com.acmed.his.exceptions;

import com.acmed.his.constants.StatusCode;
import lombok.Data;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class BaseException extends RuntimeException {
    private StatusCode statusCode;//状态码: 参见StatusCode
    private String msg;

    public BaseException(StatusCode statusCode){
        this.statusCode = statusCode;
        this.msg = statusCode.getErrorMsg();
    }
}
