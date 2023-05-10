package com.example.rabbitmqdemo;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.concurrent.TimeoutException;

public class Send3 {

	final static String EXCHANGE_NAME = "tx_exchange";
	final static String QUEUE_NAME = "tx_queue";
	public static void main(String[] args) throws IOException, TimeoutException {

		ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
		Connection conn = connFactory.newConnection();
		Channel channel = conn.createChannel();
		channel.txSelect();
		String msgTemplate = "测试消息内容[%d]";
		channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT, true, false, new HashMap<>());
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "tx");

		channel.basicPublish(EXCHANGE_NAME,"tx",new AMQP.BasicProperties(),String.format(msgTemplate,1).getBytes(StandardCharsets.UTF_8));
		channel.basicPublish(EXCHANGE_NAME,"tx",new AMQP.BasicProperties(),String.format(msgTemplate,2).getBytes(StandardCharsets.UTF_8));
		channel.basicPublish(EXCHANGE_NAME,"tx",new AMQP.BasicProperties(),String.format(msgTemplate,3).getBytes(StandardCharsets.UTF_8));
		channel.txRollback();

		channel.basicPublish(EXCHANGE_NAME,"tx",new AMQP.BasicProperties(),String.format(msgTemplate,4).getBytes(StandardCharsets.UTF_8));
		channel.txCommit();
		channel.basicPublish(EXCHANGE_NAME,"tx",new AMQP.BasicProperties(),String.format(msgTemplate,4).getBytes(StandardCharsets.UTF_8));
		channel.txCommit();
		channel.basicPublish(EXCHANGE_NAME,"tx",new AMQP.BasicProperties(),String.format(msgTemplate,4).getBytes(StandardCharsets.UTF_8));
		channel.txCommit();
	}

}
