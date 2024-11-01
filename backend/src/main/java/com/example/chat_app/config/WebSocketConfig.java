package com.example.chat_app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import jakarta.annotation.Nonnull;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(@Nonnull MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic", "/queue"); // メッセージブローカーの設定
        config.setApplicationDestinationPrefixes("/app"); // アプリケーションのプレフィックス
    }

    @Override
    public void registerStompEndpoints(@Nonnull StompEndpointRegistry registry) {
        registry.addEndpoint("/chat-websocket") // WebSocketエンドポイント
                //.setHandshakeHandler(new CustomHandshakeHandler()) // 認証情報を維持
                .setAllowedOrigins("http://localhost:3000") // フロントエンドのURLを指定
                .withSockJS();// SockJSを使用する場合
    }
}