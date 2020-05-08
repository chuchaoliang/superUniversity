package com.ccl.wx.config.websocket;

import com.ccl.wx.global.handler.WsHandler;
import com.ccl.wx.global.incerceptor.WsInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author 褚超亮
 * @date 2020/5/6 10:20
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    @Resource
    private WsHandler wsHandler;
    @Resource
    private WsInterceptor wsInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(wsHandler, "ws")
                .addInterceptors(wsInterceptor)
                .setAllowedOrigins("*");
    }
}
