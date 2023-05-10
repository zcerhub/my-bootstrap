package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
        Connection conn = connFactory.newConnection();
        Channel chan = conn.createChannel();
        chan.queueDeclare(Send.QUEUE_NAME, false, false, false, null);

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println(msg);
        };

        chan.basicConsume(Send.QUEUE_NAME, true, deliverCallback, consumerTag -> {
        });


    }

}
