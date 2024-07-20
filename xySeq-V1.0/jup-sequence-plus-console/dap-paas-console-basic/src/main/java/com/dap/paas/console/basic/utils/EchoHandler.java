package com.dap.paas.console.basic.utils;

import com.dap.paas.console.common.constants.WebParamConsts;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;


@Component
public class EchoHandler extends TextWebSocketHandler {

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        session.getAttributes().put(WebParamConsts.WEBSOCKET_LAST_PACKET,System.currentTimeMillis());
        super.handleMessage(session, message);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        session.getAttributes().put(WebParamConsts.WEBSOCKET_LAST_PACKET,System.currentTimeMillis());
        Object logId = session.getAttributes().get("logId");
        if(logId != null && logId instanceof String){
            //初始发送所有消息
            String payload = OperateLogHolder.get((String) logId);
            if(StringUtils.isNotBlank(payload)){
                session.sendMessage(new TextMessage(payload));
            }
            LogSubscriberHolder.addSession(String.valueOf(logId),session);
        }else{
            session.close(CloseStatus.NOT_ACCEPTABLE);
        }
    }


    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        LogSubscriberHolder.removeSession(session);
    }
}
