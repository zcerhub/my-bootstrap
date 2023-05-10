package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send2 {

	final static String QUEUE_NAME = "hello";
	public static void main(String[] args) {

		ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
		try (Connection conn=connFactory.newConnection()){
			Channel channel = conn.createChannel();

			channel.queueDeclare("task_queue", true, false, false, null);
			String[] strArr=new String[]{"First message.","Second message..","Third message...","Fourth message....","Fifth message....."};
			for(String msg:strArr){
				channel.basicPublish("","task_queue", MessageProperties.PERSISTENT_TEXT_PLAIN,msg.getBytes());
				System.out.println(" [x] Send '"+msg+"' ");
			}
		} catch (IOException |TimeoutException e) {
			throw new RuntimeException(e);
		}  

	}

}
