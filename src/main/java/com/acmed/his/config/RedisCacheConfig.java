package com.acmed.his.config;/**
 * Created by Eric on 2017-05-15.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis服务
 *
 * @Author Eric
 * @Version 2017-05-15 18:04
 **/
@Configuration
public class RedisCacheConfig extends CachingConfigurerSupport {

    @Autowired
    private RedisConfiguration redisConfiguration;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();

        String ip = redisConfiguration.getIp();//Redis IP
        int port = Integer.parseInt(redisConfiguration.getPort());//Redis Port
        String password = redisConfiguration.getPassword();//Redis Password

        redisConnectionFactory.setHostName(ip);
        redisConnectionFactory.setPort(port);
        redisConnectionFactory.setPassword(password);
        return redisConnectionFactory;
    }

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory cf) {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<String, String>();
        redisTemplate.setConnectionFactory(cf);
        return redisTemplate;
    }

    @Bean
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);

        // Number of seconds before expiration. Defaults to unlimited (0)
        cacheManager.setDefaultExpiration(3000); // Sets the default expire time (in seconds)
        return cacheManager;
    }

}