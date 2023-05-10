package com.example.log4jdemo;


import java.util.logging.Logger;

public class Main {

    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(final String... args)
    {
        logger.debug("Debug Message Logged !!!");
        logger.info("Info Message Logged !!!");
        logger.error("Error Message Logged !!!", new NullPointerException("NullError"));
    }
}
