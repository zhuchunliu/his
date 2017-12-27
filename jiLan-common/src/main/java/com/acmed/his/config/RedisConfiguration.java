package com.acmed.his.config;/**
 * Created by Eric on 2017-05-23.
 */

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Redis配置类
 *
 * @Author Eric
 * @Version 2017-05-23 13:31
 **/
@Component
@ConfigurationProperties(prefix = "redis")
public class RedisConfiguration {
    private String ip;
    private String port;
    private String password;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
