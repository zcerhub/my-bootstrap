package com.example.loggingdemo;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

public class Main {

/*	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class.getName());
		logger.info("hello, world!");
	}*/

/*	public static void main(String[] args) {
		Log log = LogFactory.getLog(Main.class);
		log.info("hello, world!");
	}*/

/*	public static void main(String[] args) {
		Logger logger = LogManager.getLogger(Main.class);
		logger.error("hello, world!");
	}*/

	public static void main(String[] args) {
		MDC.put("trackId",String.valueOf(111111));
		Logger logger = LoggerFactory.getLogger(Main.class);
		logger.info("hello, world!");
	}


}
