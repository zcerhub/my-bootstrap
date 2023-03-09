package com.example.loggingdemo;


import org.apache.log4j.Logger;

public class LoggingDemoApplication {

	public static void main(String[] args) {
		Logger logger = Logger.getLogger(LoggingDemoApplication.class);
		logger.info("hello, world!");
	}

}
