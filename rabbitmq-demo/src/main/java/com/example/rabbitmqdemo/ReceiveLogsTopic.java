package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ReceiveLogsTopic {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
        Connection conn = connFactory.newConnection();
        Channel chan = conn.createChannel();

        chan.exchangeDeclare(EmitLogTopic.EXCHANGE_NAME, "topic");
        String queueName = chan.queueDeclare().getQueue();
//        chan.queueBind(queueName, EmitLogTopic.EXCHANGE_NAME, "#");
//        chan.queueBind(queueName, EmitLogTopic.EXCHANGE_NAME, "kern.*");
        chan.queueBind(queueName, EmitLogTopic.EXCHANGE_NAME, "*.critical");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), "UTF-8");
            System.out.println(delivery.getEnvelope().getRoutingKey() + " " + msg);
        };

        chan.basicConsume(queueName, true, deliverCallback, consumerTag -> {
        });

    }


}
