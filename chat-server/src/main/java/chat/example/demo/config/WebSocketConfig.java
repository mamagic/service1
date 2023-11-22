package chat.example.demo.config;

import chat.example.demo.handler.StompExceptionHandler;
import chat.example.demo.handler.StompHandler;
import chat.example.demo.interceptor.StompHandShakeInterceptor;
import chat.example.demo.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import java.util.Map;


@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Autowired
    private final StompHandler stompHandler;
    @Autowired
    private final StompExceptionHandler stompExceptionHandler;
    @Autowired
    private final StompHandShakeInterceptor stompHandShakeInterceptor;


    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) { // (2)
        registry.enableSimpleBroker("/sub"); // (3)
        registry.setApplicationDestinationPrefixes("/notifications"); // (4)
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // (5)
        registry
            .setErrorHandler(stompExceptionHandler)
            .addEndpoint("/ws") // ex ) ws://localhost:9000/stomp/chat
            .setAllowedOriginPatterns("*").withSockJS();
            //.addInterceptors(stompHandShakeInterceptor).withSockJS();
    }

//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }
}
