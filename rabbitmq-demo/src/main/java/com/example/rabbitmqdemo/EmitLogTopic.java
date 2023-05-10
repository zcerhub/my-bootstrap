package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLogTopic {

    static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
        Connection conn = connFactory.newConnection();
        Channel chan = conn.createChannel();

        chan.exchangeDeclare(EXCHANGE_NAME, "topic");

        String[] serverityArr = new String[]{"kern.critical"};
        String[] msgArr = new String[]{"A critical kernel error"};

        for (int i = 0; i < msgArr.length; i++) {
            chan.basicPublish(EXCHANGE_NAME,serverityArr[i],null,msgArr[i].getBytes());
            System.out.println(i);
        }

    }

}
