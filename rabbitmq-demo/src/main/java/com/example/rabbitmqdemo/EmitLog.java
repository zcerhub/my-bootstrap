package com.example.rabbitmqdemo;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class EmitLog {

	final static String EXCHANGE_NAME = "logs";
	public static void main(String[] args) {

		ConnectionFactory connFactory = ConnFacotryUtil.getConnectionFactory();
		try (Connection conn=connFactory.newConnection()){
			Channel channel = conn.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

			String[] strArr=new String[]{"First message.","Second message..","Third message...","Fourth message....","Fifth message....."};
			for(String msg:strArr){
				channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
				System.out.println(" [x] Send '"+msg+"' ");
			}
		} catch (IOException |TimeoutException e) {
			throw new RuntimeException(e);
		}  

	}

}
