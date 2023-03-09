package com.example.loggingdemo;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		Logger logger = LogManager.getLogger(Main.class);
		logger.error("hello, world!");
	}

}
