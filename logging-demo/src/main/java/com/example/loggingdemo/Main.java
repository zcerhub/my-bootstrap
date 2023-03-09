package com.example.loggingdemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

/*	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class.getName());
		logger.info("hello, world!");
	}*/

/*	public static void main(String[] args) {
		Log log = LogFactory.getLog(Main.class);
		log.info("hello, world!");
	}*/

	public static void main(String[] args) {
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("hello, world!");
	}

}
