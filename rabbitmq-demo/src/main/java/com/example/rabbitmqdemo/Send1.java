package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.Stack;
import java.util.concurrent.TimeoutException;

public class Send1 {

	final static String QUEUE_NAME = "hello";
	public static void main(String[] args) {

		ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
		try (Connection conn=connFactory.newConnection()){
			Channel channel = conn.createChannel();

			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String[] strArr=new String[]{"First message.","Second message..","Third message...","Fourth message....","Fifth message....."};
			for(String msg:strArr){
				channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
				System.out.println(" [x] Send '"+msg+"' ");
			}
		} catch (IOException |TimeoutException e) {
			throw new RuntimeException(e);
		}  

	}

}
