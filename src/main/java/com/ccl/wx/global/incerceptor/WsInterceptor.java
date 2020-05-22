package com.ccl.wx.global.incerceptor;

import cn.hutool.http.HttpUtil;
import com.ccl.wx.config.websocket.WsSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author 褚超亮
 * @date 2020/5/6 10:38
 */
@Slf4j
@Component
public class WsInterceptor implements HandshakeInterceptor {
    /**
     * 握手前
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param attributes
     * @return
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) {
        log.info("握手开始");
        // 获得请求参数
        HashMap<String, String> paramMap = HttpUtil.decodeParamMap(request.getURI().getQuery(), "utf-8");
        String uid = paramMap.get("token");
        // 检测用户是否存在
        if (!StringUtils.isEmpty(uid)) {
            // 判断token是否重复连接
            Set<String> keySet = WsSession.getSessionPool().keySet();
            if (keySet.contains(uid)) {
                log.error("用户" + uid + "握手失败！！！");
                return false;
            } else {
                // 放入属性域
                attributes.put("token", uid);
                log.info("用户" + uid + "握手成功！！！");
                return true;
            }
        }
        log.error("用户" + uid + "握手失败！！！");
        return false;
    }

    /**
     * 握手后
     *
     * @param request
     * @param response
     * @param wsHandler
     * @param exception
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("握手完成！！！");
    }
}
