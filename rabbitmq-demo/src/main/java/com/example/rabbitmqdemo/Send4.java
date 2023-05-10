package com.example.rabbitmqdemo;

import com.rabbitmq.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Slf4j
public class Send4 {

	final static String EXCHANGE_NAME = "confirm_exchange";
	final static String QUEUE_NAME = "confirm_queue";
	public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

		ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
		Connection conn = connFactory.newConnection();
		Channel channel = conn.createChannel();
		String msgTemplate = "测试消息内容[%d]";
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, new HashMap<>());
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "tx");

		channel.confirmSelect();

		channel.addConfirmListener(new ConfirmListener() {
			@Override
			public void handleAck(long deliveryTag, boolean multiple) throws IOException {
				log.info("ack: deliveryTag = {},multiple = {}",deliveryTag,multiple);
			}

			@Override
			public void handleNack(long deliveryTag, boolean multiple) throws IOException {
				log.error("nack: deliveryTag = {},multiple = {}",deliveryTag,multiple);
			}
		});

		for (int i = 0; i < 1077; i++) {
			channel.basicPublish(EXCHANGE_NAME,"tx",new AMQP.BasicProperties(),String.format(msgTemplate,i).getBytes(StandardCharsets.UTF_8));
//			TimeUnit.SECONDS.sleep(1);
		}
	}

}
