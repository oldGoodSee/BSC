package com.bocom.support.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyRunTimeException extends RuntimeException {

    private static final Logger logger = LoggerFactory
            .getLogger(MyRunTimeException.class);

    public MyRunTimeException(String msg) {
        super(msg);
        logger.error(msg);
    }
}
