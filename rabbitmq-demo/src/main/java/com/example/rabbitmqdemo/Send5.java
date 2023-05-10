package com.example.rabbitmqdemo;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Send5 {

	final static String EXCHANGE_NAME = "mandatory_exchange";
	final static String QUEUE_NAME = "mandatory_queue";
	final static String ROUTE_KEY = "mandatory-1";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

		ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
		Connection conn = connFactory.newConnection();
		Channel channel = conn.createChannel();
		String msgTemplate = "测试消息内容[%d]";
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, new HashMap<>());
//		channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "mandatory");

		channel.addReturnListener(new ReturnListener() {
			@Override
			public void handleReturn(int replyCode, java.lang.String replyText, java.lang.String exchange, java.lang.String routingKey, AMQP.BasicProperties properties, byte[] body) throws IOException {
				log.error("replyCode = {},replyText = {},exchange = {},routingKey = {},body = {}",replyCode,replyText,exchange,routingKey,new String(body));
			}
		});

		channel.basicPublish(EXCHANGE_NAME,ROUTE_KEY,true,new AMQP.BasicProperties(),String.format(msgTemplate,1).getBytes(StandardCharsets.UTF_8));
	}

}
