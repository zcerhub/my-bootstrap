package com.dap.paas.console.basic.utils;

import com.dap.paas.console.common.util.ANSIUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;


@Slf4j
public class WebSocketEcho implements EchoFunction {
    @Override
    public void print(String logId, String line) {
        line = HtmlUtils.htmlEscape(line);
        WebSocketSession session = LogSubscriberHolder.getSession(logId);
        synchronized (this) {
            if(session == null){
                OperateLogHolder.append(logId,line);
            }else{
                try {
                    Date date = new Date();
                    SimpleDateFormat sbf = new SimpleDateFormat("HH:mm:ss");
                    session.sendMessage(new TextMessage(ANSIUtil.convertHtml("[" + sbf.format(date) +  "] " + line)));
                } catch (IOException e) {
                    log.error("",e);
                }
            }
        }
    }

    @Override
    public void close(String logId) {
        try {
            TimeUnit.SECONDS.sleep(2);//休息2秒，防止报错情况下，关闭的时候客户端尚未连接
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        WebSocketSession session = LogSubscriberHolder.getSession(logId);
        if(session != null){
            try {
                LogSubscriberHolder.removeSession(logId);
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
