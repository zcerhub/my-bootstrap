package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogs {


    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
        Connection conn = connFactory.newConnection();
        Channel chan = conn.createChannel();

        chan.exchangeDeclare(EmitLog.EXCHANGE_NAME, "fanout");
        String queueName = chan.queueDeclare().getQueue();
        System.out.println(queueName);
        chan.queueBind(queueName, EmitLog.EXCHANGE_NAME, "");

        System.out.println("等待接收日志");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println(msg);
        };

        chan.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });


    }

}
