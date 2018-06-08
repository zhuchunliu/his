package com.acmed.his.config;

import com.acmed.his.constants.StatusCode;
import com.acmed.his.exceptions.BaseException;
import com.acmed.his.model.User;
import com.acmed.his.pojo.RequestToken;
import com.acmed.his.util.ResponseResult;
import com.acmed.his.util.TokenUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import java.security.Principal;
import java.util.Optional;

/**
 * Created by Darren on 2018-06-08
 **/
@Configuration
@EnableWebSocketMessageBroker
public class WebsocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        super.configureMessageBroker(registry);
        registry.setUserDestinationPrefix("/callback");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry stompEndpointRegistry) {
        stompEndpointRegistry.addEndpoint("/client").setAllowedOrigins("*").withSockJS();
    }


    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(100).keepAliveSeconds(60);
        registration.interceptors(new ChannelInterceptorAdapter(){
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
                if(StompCommand.CONNECT.equals(accessor.getCommand())){
                    final String token = accessor.getNativeHeader("Authorization").get(0);
                    String loginId = Optional.ofNullable(token)
                            .map(TokenUtil::getFromToken).map(ResponseResult::getResult)
                            .map(val ->(RequestToken)val).map(RequestToken::getLoginid).orElse(null);
                    if(loginId.startsWith("USER_PAD")) {//刷新token有效期
                        String userId = loginId.substring("USER_PAD".length());
                        accessor.setUser(new Principal() {//为每一个用户创建特有的principal
                            @Override
                            public String getName() {
                                return userId;
                            }
                        });
                    }
                    return message;
                }
                return super.preSend(message, channel);
            }
        });
    }

    /**
     * @param registration
     */
    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.taskExecutor().corePoolSize(4).maxPoolSize(8);
    }
}
