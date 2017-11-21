package com.acmed.his.pojo;/**
 * Created by Eric on 2017-05-11.
 */

import lombok.Data;
import lombok.extern.log4j.Log4j;

import java.io.Serializable;

/**
 * 用户验证类
 *
 * @Author Eric
 * @Version 2017-05-11 16:21
 **/

@Log4j
public @Data
class RequestToken implements Serializable {
    private String token;//token用户调用api接口
    private String loginid;//请求用户id,appkey
    private String category;//请求类型
    private String remoteip;//访问客户端ip地址
}
