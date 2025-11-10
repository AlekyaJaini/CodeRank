package org.example.util;

import org.slf4j.LoggerFactory;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogUtils {
    private static final Logger log = LoggerFactory.getLogger(LogUtils.class);

    private LogUtils() {}

    public static void info(String message) {
        log.info(message);
    }

    public static void error(String message, Throwable t) {
        log.error(message, t);
    }
}
