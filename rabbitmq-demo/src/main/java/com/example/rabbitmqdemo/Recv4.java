package com.example.rabbitmqdemo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Recv4 {

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
        Connection conn = connFactory.newConnection();
        Channel chan = conn.createChannel();
        chan.queueDeclare(Send.QUEUE_NAME, false, false, false, null);

        Consumer consumer = new DefaultConsumer(chan) {
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body)
                    throws IOException {
                System.out.println("收到消息：" + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                chan.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        chan.basicConsume(Send.QUEUE_NAME, consumer);
        TimeUnit.SECONDS.sleep(10);
        chan.close();
        conn.close();
    }

}
