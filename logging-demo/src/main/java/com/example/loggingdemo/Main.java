package com.example.loggingdemo;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.logging.Logger;

public class Main {

/*	public static void main(String[] args) {
		Logger logger = Logger.getLogger(Main.class.getName());
		logger.info("hello, world!");
	}*/

	public static void main(String[] args) {
		Log log = LogFactory.getLog(Main.class);
		log.info("hello, world!");
	}

}
