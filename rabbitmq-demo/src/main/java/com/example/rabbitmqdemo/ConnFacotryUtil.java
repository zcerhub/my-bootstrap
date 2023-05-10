package com.example.rabbitmqdemo;

import com.rabbitmq.client.ConnectionFactory;

public class ConnFacotryUtil {

    public static ConnectionFactory getConnectionFactory() {
        ConnectionFactory factory = new ConnectionFactory();
        String rabbitHost="localhost";
        factory.setHost(rabbitHost);
        factory.setUsername("guest");
        factory.setPassword("guest");
        return factory;
    }

}
