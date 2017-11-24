package com.acmed.his.exceptions;

import com.acmed.his.constants.StatusCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * Created by Darren on 2017-11-24
 **/
@Data
public class ErrorMsg {
    @ApiModelProperty(value="返回状态 success / error")
    private  boolean success=false;//返回状态：success / error
    @ApiModelProperty(value="返回状态代码")
    private StatusCode statusCode;//状态码: 参见StatusCode
    @ApiModelProperty(value="返回消息")
    private String msg;

    public ErrorMsg(BaseException ex){
        this.statusCode = ex.getStatusCode();
        this.msg = this.statusCode.getErrorMsg();
    }
}
