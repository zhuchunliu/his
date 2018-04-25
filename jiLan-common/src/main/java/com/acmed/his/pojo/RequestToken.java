package com.acmed.his.pojo;/**
 * Created by Eric on 2017-05-11.
 */

import com.alibaba.fastjson.annotation.JSONField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 用户验证类
 *
 * @Author Eric
 * @Version 2017-05-11 16:21
 **/

public @Data
class RequestToken {
    @ApiModelProperty("token")
    private String token;//token用户调用api接口
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String loginid;//请求用户id,appkey
    @JSONField(serialize = false)
    @ApiModelProperty(hidden = true)
    private String remoteip;//访问客户端ip地址
    @ApiModelProperty("用户角色 1:患者； 2:医生/患者")
    private Integer status;//0:管理员 1:患者  2:医生/患者

    public RequestToken(){

    }

}
