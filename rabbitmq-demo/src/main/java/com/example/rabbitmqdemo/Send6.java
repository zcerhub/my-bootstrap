package com.example.rabbitmqdemo;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Send6 {

	final static String EXCHANGE_NAME = "durable_exchange";
	final static String QUEUE_NAME = "durable_queue";
	final static String ROUTE_KEY = "durable";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

		ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
		Connection conn = connFactory.newConnection();
		Channel channel = conn.createChannel();
		String msgTemplate = "测试消息内容[%d]";
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, new HashMap<>());
		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTE_KEY);

		channel.basicPublish(EXCHANGE_NAME,ROUTE_KEY,true,MessageProperties.PERSISTENT_TEXT_PLAIN,String.format(msgTemplate,1).getBytes(StandardCharsets.UTF_8));
	}

}
